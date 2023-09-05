#include <opencv2/opencv.hpp>
#include <iostream>

int main() {
	// 读取图像
	cv::Mat image = cv::imread("D:\\path_to_your_image.jpg");

	if (image.empty()) {
		std::cerr << "Error: Unable to load image." << std::endl;
		return -1;
	}

	// 创建一个核（用于定义腐蚀的形状和大小）
	cv::Mat kernel = cv::getStructuringElement(cv::MORPH_RECT, cv::Size(5, 5));

	// 用来存放腐蚀之后的图片
	cv::Mat erodedImage;

	// 执行腐蚀操作
	cv::erode(image, erodedImage, kernel);

	// 显示原始图像和腐蚀后的图像
	cv::imshow("原始图像", image);
	cv::imshow("腐蚀后的图像", erodedImage);

	// 等待用户按下键盘上的任意键
	cv::waitKey(0);

	return 0;
}
