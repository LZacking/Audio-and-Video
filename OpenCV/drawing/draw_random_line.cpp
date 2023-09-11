#include <opencv2/opencv.hpp>
#include <cstdlib> // 包含随机数生成器所需的头文件

int main() {
	// 创建一个空白图像
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // 白色背景

	// 随机生成直线的参数
	int x1 = rand() % image.cols; // 随机生成起点 x 坐标
	int y1 = rand() % image.rows; // 随机生成起点 y 坐标
	int x2 = rand() % image.cols; // 随机生成终点 x 坐标
	int y2 = rand() % image.rows; // 随机生成终点 y 坐标

	// 随机生成直线的颜色（BGR格式）
	cv::Scalar color(rand() % 256, rand() % 256, rand() % 256);

	// 随机生成直线的线宽
	int thickness = rand() % 5 + 1; // 随机生成1到5之间的整数作为线宽

	// 使用随机参数绘制直线
	cv::line(image, cv::Point(x1, y1), cv::Point(x2, y2), color, thickness);

	// 显示图像窗口并等待按键
	cv::imshow("Random Line Drawing", image);
	cv::waitKey(0);

	return 0;
}
