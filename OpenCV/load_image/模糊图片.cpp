#include <opencv2/opencv.hpp>
#include <iostream>

int main() {
	// ��ȡͼ��
	cv::Mat image = cv::imread("D:\\path_to_your_image.jpg");

	if (image.empty()) {
		std::cerr << "Error: Unable to load image." << std::endl;
		return -1;
	}

	// ����ģ���˵Ĵ�С
	cv::Size kernelSize(5, 5); // ���Ը�����Ҫ�����˵Ĵ�С

	// �������ģ��֮���ͼƬ
	cv::Mat blurredImage;
	// ִ�и�˹ģ������
	cv::GaussianBlur(image, blurredImage, kernelSize, 0);

	// ��ʾԭʼͼ���ģ�����ͼ��
	cv::imshow("ԭʼͼ��", image);
	cv::imshow("ģ�����ͼ��", blurredImage);

	// �ȴ��û����¼����ϵ������
	cv::waitKey(0);

	return 0;
}
