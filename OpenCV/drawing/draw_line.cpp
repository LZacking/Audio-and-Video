#include <opencv2/opencv.hpp>

int main() {
	// 创建一个空白图像
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // 白色背景

	// 定义直线的起点和终点坐标
	cv::Point start(100, 100);
	cv::Point end(500, 300);

	// 指定直线的颜色（BGR格式）和线宽
	cv::Scalar color(0, 0, 255); // 红色
	int thickness = 2;

	// 在图像上绘制直线
	cv::line(image, start, end, color, thickness);

	// 显示图像窗口并等待按键
	cv::imshow("Line Drawing", image);
	cv::waitKey(0);

	return 0;
}
