#include <opencv2/opencv.hpp>
#include <iostream>

int main() {
	// ��ȡͼ��
	cv::Mat image = cv::imread("D:\\path_to_your_image.jpg");

	if (image.empty()) {
		std::cerr << "Error: Unable to load image." << std::endl;
		return -1;
	}

	// ������ű�Ե���֮���ͼƬ
	cv::Mat edges;
	// ִ��Canny��Ե���
	cv::Canny(image, edges, 100, 200); // ������ֵ�Կ��Ʊ�Ե����������

	// ��ʾԭʼͼ��ͱ�Ե�����
	cv::imshow("ԭʼͼ��", image);
	cv::imshow("��Ե�����ͼ��", edges);

	// �ȴ��û����¼����ϵ������
	cv::waitKey(0);

	return 0;
}
