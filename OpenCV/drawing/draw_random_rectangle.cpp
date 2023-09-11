#include <opencv2/opencv.hpp>
#include <cstdlib> // 包含随机数生成器所需的头文件

int main() {
	// 创建一个空白图像
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // 白色背景

	// 随机生成矩形的参数
	int x = rand() % image.cols; // 随机生成矩形左上角 x 坐标
	int y = rand() % image.rows; // 随机生成矩形左上角 y 坐标
	int width = rand() % 200 + 50; // 随机生成宽度，范围在50到250之间
	int height = rand() % 150 + 50; // 随机生成高度，范围在50到200之间

	// 随机生成矩形的颜色（BGR格式）
	cv::Scalar color(rand() % 256, rand() % 256, rand() % 256);

	// 随机生成矩形的线宽
	int thickness = rand() % 5 + 1; // 随机生成1到5之间的整数作为线宽

	// 使用随机参数绘制矩形
	cv::Rect rectangle(x, y, width, height);
	cv::rectangle(image, rectangle, color, thickness);

	// 显示图像窗口并等待按键
	cv::imshow("Random Rectangle Drawing", image);
	cv::waitKey(0);

	return 0;
}
