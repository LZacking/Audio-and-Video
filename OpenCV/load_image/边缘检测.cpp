#include <opencv2/opencv.hpp>
#include <iostream>

int main() {
	// 读取图像
	cv::Mat image = cv::imread("D:\\path_to_your_image.jpg");

	if (image.empty()) {
		std::cerr << "Error: Unable to load image." << std::endl;
		return -1;
	}

	// 用来存放边缘检测之后的图片
	cv::Mat edges;
	// 执行Canny边缘检测
	cv::Canny(image, edges, 100, 200); // 调整阈值以控制边缘检测的灵敏度

	// 显示原始图像和边缘检测结果
	cv::imshow("原始图像", image);
	cv::imshow("边缘检测后的图像", edges);

	// 等待用户按下键盘上的任意键
	cv::waitKey(0);

	return 0;
}
