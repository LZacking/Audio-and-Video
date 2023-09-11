#include <opencv2/opencv.hpp>

int main() {
	// 创建一个空白图像
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // 白色背景

	// 定义圆心坐标
	cv::Point center(300, 200);

	// 定义圆的半径
	int radius = 50;

	// 指定圆的颜色（BGR格式）和线宽
	cv::Scalar color(0, 0, 255); // 红色
	int thickness = 2;

	// 在图像上绘制圆
	cv::circle(image, center, radius, color, thickness);

	// 显示图像窗口并等待按键
	cv::imshow("Circle Drawing", image);
	cv::waitKey(0);

	return 0;
}
