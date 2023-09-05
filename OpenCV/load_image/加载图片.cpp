#include <opencv2/opencv.hpp>
#include <iostream>

// ����ͼƬ
int main() {
	// ����ͼƬ�ļ�������·��
	std::string image_path = "D:\\path_to_your_image.jpg";

	// ʹ��OpenCV����ͼƬ
	cv::Mat image = cv::imread(image_path);

	if (image.empty()) {
		std::cerr << "Error: Unable to load image." << std::endl;
		return -1;
	}

	cv::imshow("Loaded Image", image);
	cv::waitKey(0);

	return 0;
}
