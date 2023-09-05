#include <opencv2/opencv.hpp>
#include <iostream>

int main() {
	// 读取彩色图像
	cv::Mat image = cv::imread("D:\\path_to_your_image.jpg");

	if (image.empty()) {
		std::cerr << "Error: Unable to load image." << std::endl;
		return -1;
	}

	// 用来存放边缘检测之后的图片
	cv::Mat edges;
	// 执行Canny边缘检测
	cv::Canny(image, edges, 100, 200); // 调整阈值以控制边缘检测的灵敏度

	// 保存边缘检测结果到D盘
	cv::imwrite("D:\\edges.jpg", edges);

	std::cout << "Edges saved to D:\\edges.jpg" << std::endl;

	return 0;
}