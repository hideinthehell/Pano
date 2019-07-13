#include <jni.h>
#include <opencv2/opencv.hpp>
#include <opencv2/core/base.hpp>
#import "opencv2/stitching.hpp"
#import "opencv2/imgcodecs.hpp"

#include <android/log.h>
#include <android/bitmap.h>

#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,"liuping", __VA_ARGS__)
using namespace cv;
using namespace std;

cv::Mat finalMat;
int BLACK_NUM = 500;

//是否为需要删除的行
bool isBlackRow(cv::Mat mat) {

    int sum = 0;
    for (int i = 0; i < BLACK_NUM; i++) {
        sum += mat.at<Vec3b>(0, i)[0];
        sum += mat.at<Vec3b>(0, i)[1];
        sum += mat.at<Vec3b>(0, i)[2];
    }
    if (sum == 0) return true;

    for (int col = 0; col < mat.cols - BLACK_NUM; col++) {
        sum -= mat.at<Vec3b>(0, col)[0];
        sum -= mat.at<Vec3b>(0, col)[1];
        sum -= mat.at<Vec3b>(0, col)[2];

        sum += mat.at<Vec3b>(0, col + BLACK_NUM)[0];
        sum += mat.at<Vec3b>(0, col + BLACK_NUM)[1];
        sum += mat.at<Vec3b>(0, col + BLACK_NUM)[2];

        if (sum == 0) return true;
    }
    return false;
}

//是否为需要删除的列
bool isBlackCol(cv::Mat mat) {
    int sum = 0;
    for (int i = 0; i < BLACK_NUM; i++) {
        sum += mat.at<Vec3b>(i, 0)[0];
        sum += mat.at<Vec3b>(i, 0)[1];
        sum += mat.at<Vec3b>(i, 0)[2];
    }
    if (sum == 0)return true;

    for (int row = 0; row < mat.rows - BLACK_NUM; row++) {
        sum -= mat.at<Vec3b>(row, 0)[0];
        sum -= mat.at<Vec3b>(row, 0)[1];
        sum -= mat.at<Vec3b>(row, 0)[2];

        sum += mat.at<Vec3b>(row + BLACK_NUM, 0)[0];
        sum += mat.at<Vec3b>(row + BLACK_NUM, 0)[1];
        sum += mat.at<Vec3b>(row + BLACK_NUM, 0)[2];

        if (sum == 0)return true;

    }

    return false;
}


cv::Mat removeBlackEdge(cv::Mat srcMat, jfloat widthRatio, jfloat heightRatio) {

    //灰度
//    cv::Mat grayMat(srcMat.cols, srcMat.rows, CV_8UC1);
//    cv::cvtColor(srcMat, grayMat, CV_BGR2GRAY);

    int topRow = 0;
    int leftCol = 0;
    int rightCol = srcMat.cols - 1;
    int bottomRow = srcMat.rows - 1;

    // 上方黑边判断
    jint limitTop = srcMat.rows * heightRatio;
    for (int row = 0; row < limitTop; row++) {
        if (isBlackRow(srcMat.row(row))) {
            topRow = row;
        } else {
            break;
        }
    }

    // 下方黑边判断
    jint limitBottom = srcMat.rows * (1 - heightRatio);
    for (int row = srcMat.rows - 1; row > limitBottom; row--) {
        if (isBlackRow(srcMat.row(row))) {
            bottomRow = row;
        } else {
            break;
        }
    }

    // 左边黑边判断
    jint limitLeft = srcMat.cols * widthRatio;
    for (int col = 0; col < limitLeft; col++) {
        if (isBlackCol(srcMat.col(col))) {
            leftCol = col;
        } else {
            break;
        }
    }

    // 右边黑边判断
    jint limitRight = srcMat.cols * (1 - widthRatio);
    for (int col = srcMat.cols - 1; col > limitRight; col--) {
        if (isBlackCol(srcMat.col(col))) {
            rightCol = col;
        } else {
            break;
        }
    }

    int x = leftCol;
    int y = topRow;
    int width = rightCol - leftCol;
    int height = bottomRow - topRow;
    cv::Rect rectR(x, y, width, height);
    cv::Mat resultMat = srcMat(rectR);

    LOGD("result  L:%d,T:%d R:%d,B:%d W:%d,H:%d", leftCol, topRow, rightCol, bottomRow, srcMat.cols,
         srcMat.rows);

    srcMat.release();

    return resultMat;
}

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_funsnap_pano_ImagesStitch_stitchImages(JNIEnv *env, jclass type,
                                                jobjectArray paths, jstring outPath,
                                                jfloat widthRatio, jfloat heightRatio,
                                                jint length) {

    BLACK_NUM = length;

    jstring jstr;
    jsize len = env->GetArrayLength(paths);
    std::vector<cv::Mat> mats;
    for (int i = 0; i < len; i++) {
        jstr = (jstring) env->GetObjectArrayElement(paths, i);
        const char *path = (char *) env->GetStringUTFChars(jstr, 0);
        cv::Mat mat = cv::imread(path);
        mats.push_back(mat);
    }

    cv::Mat temMat;
    cv::Stitcher stitcher = cv::Stitcher::createDefault(false);
    Stitcher::Status state = stitcher.stitch(mats, temMat);

    //拼接成功
    if (state == 0) {
        //裁剪掉黑边
        finalMat = removeBlackEdge(temMat, widthRatio, heightRatio);
        //保存mat到本地
        String path = env->GetStringUTFChars(outPath, JNI_FALSE);
        imwrite(path, finalMat);
        finalMat.release();
    }

    jintArray jint_arr = env->NewIntArray(3);
    jint *elems = env->GetIntArrayElements(jint_arr, NULL);
    elems[0] = state;//状态码
    elems[1] = finalMat.cols;//宽
    elems[2] = finalMat.rows;//高

    env->ReleaseIntArrayElements(jint_arr, elems, 0);

    //释放mat
    temMat.release();
    for (int i = 0; i < mats.size(); i++) {
        mats[i].release();
    }

    return jint_arr;
}


