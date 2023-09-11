#include <opencv2/opencv.hpp>

int main() {
	// 创建一个空白图像
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // 白色背景

	// 定义矩形的左上角和右下角坐标
	cv::Point topLeft(100, 100);
	cv::Point bottomRight(300, 200);

	// 指定矩形的颜色（BGR格式）和线宽
	cv::Scalar color(0, 0, 255); // 红色
	int thickness = 2;

	// 在图像上绘制矩形
	cv::rectangle(image, topLeft, bottomRight, color, thickness);

	// 显示图像窗口并等待按键
	cv::imshow("Rectangle Drawing", image);
	cv::waitKey(0);

	return 0;
}
