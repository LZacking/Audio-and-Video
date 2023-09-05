#include <opencv2/opencv.hpp>
#include <iostream>

int main() {
	// ��ȡ��ɫͼ��
	cv::Mat image = cv::imread("D:\\path_to_your_image.jpg");

	if (image.empty()) {
		std::cerr << "Error: Unable to load image." << std::endl;
		return -1;
	}

	// ������ű�Ե���֮���ͼƬ
	cv::Mat edges;
	// ִ��Canny��Ե���
	cv::Canny(image, edges, 100, 200); // ������ֵ�Կ��Ʊ�Ե����������

	// �����Ե�������D��
	cv::imwrite("D:\\edges.jpg", edges);

	std::cout << "Edges saved to D:\\edges.jpg" << std::endl;

	return 0;
}