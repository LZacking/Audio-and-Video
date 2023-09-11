#include <opencv2/opencv.hpp>
#include <cstdlib> // ��������������������ͷ�ļ�

int main() {
	// ����һ���հ�ͼ��
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // ��ɫ����

	// ������ɾ��εĲ���
	int x = rand() % image.cols; // ������ɾ������Ͻ� x ����
	int y = rand() % image.rows; // ������ɾ������Ͻ� y ����
	int width = rand() % 200 + 50; // ������ɿ�ȣ���Χ��50��250֮��
	int height = rand() % 150 + 50; // ������ɸ߶ȣ���Χ��50��200֮��

	// ������ɾ��ε���ɫ��BGR��ʽ��
	cv::Scalar color(rand() % 256, rand() % 256, rand() % 256);

	// ������ɾ��ε��߿�
	int thickness = rand() % 5 + 1; // �������1��5֮���������Ϊ�߿�

	// ʹ������������ƾ���
	cv::Rect rectangle(x, y, width, height);
	cv::rectangle(image, rectangle, color, thickness);

	// ��ʾͼ�񴰿ڲ��ȴ�����
	cv::imshow("Random Rectangle Drawing", image);
	cv::waitKey(0);

	return 0;
}
