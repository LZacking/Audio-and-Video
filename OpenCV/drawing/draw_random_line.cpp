#include <opencv2/opencv.hpp>
#include <cstdlib> // ��������������������ͷ�ļ�

int main() {
	// ����һ���հ�ͼ��
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // ��ɫ����

	// �������ֱ�ߵĲ���
	int x1 = rand() % image.cols; // ���������� x ����
	int y1 = rand() % image.rows; // ���������� y ����
	int x2 = rand() % image.cols; // ��������յ� x ����
	int y2 = rand() % image.rows; // ��������յ� y ����

	// �������ֱ�ߵ���ɫ��BGR��ʽ��
	cv::Scalar color(rand() % 256, rand() % 256, rand() % 256);

	// �������ֱ�ߵ��߿�
	int thickness = rand() % 5 + 1; // �������1��5֮���������Ϊ�߿�

	// ʹ�������������ֱ��
	cv::line(image, cv::Point(x1, y1), cv::Point(x2, y2), color, thickness);

	// ��ʾͼ�񴰿ڲ��ȴ�����
	cv::imshow("Random Line Drawing", image);
	cv::waitKey(0);

	return 0;
}
