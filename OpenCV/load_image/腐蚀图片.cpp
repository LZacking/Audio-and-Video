#include <opencv2/opencv.hpp>
#include <iostream>

int main() {
	// ��ȡͼ��
	cv::Mat image = cv::imread("D:\\path_to_your_image.jpg");

	if (image.empty()) {
		std::cerr << "Error: Unable to load image." << std::endl;
		return -1;
	}

	// ����һ���ˣ����ڶ��帯ʴ����״�ʹ�С��
	cv::Mat kernel = cv::getStructuringElement(cv::MORPH_RECT, cv::Size(5, 5));

	// ������Ÿ�ʴ֮���ͼƬ
	cv::Mat erodedImage;

	// ִ�и�ʴ����
	cv::erode(image, erodedImage, kernel);

	// ��ʾԭʼͼ��͸�ʴ���ͼ��
	cv::imshow("ԭʼͼ��", image);
	cv::imshow("��ʴ���ͼ��", erodedImage);

	// �ȴ��û����¼����ϵ������
	cv::waitKey(0);

	return 0;
}
