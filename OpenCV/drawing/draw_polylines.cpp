#include <opencv2/opencv.hpp>

int main() {
	// ����һ���հ�ͼ��
	cv::Mat image(400, 600, CV_8UC3, cv::Scalar(255, 255, 255)); // ��ɫ����

	// �������εĶ���
	std::vector<cv::Point> points;
	points.push_back(cv::Point(100, 100));
	points.push_back(cv::Point(300, 100));
	points.push_back(cv::Point(400, 300));
	points.push_back(cv::Point(200, 400));

	// ������εĵ�һ���㸴�Ƶ����һ���㣬�Ապ϶����
	points.push_back(points[0]);

	// ���ö���ε���ɫ��BGR��ʽ�����߿�
	cv::Scalar color(0, 0, 255); // ��ɫ
	int thickness = 2;

	// ʹ��ָ���Ķ��㡢��ɫ���߿���ƶ����
	std::vector<std::vector<cv::Point>> contours;
	contours.push_back(points);
	cv::polylines(image, contours, true, color, thickness);

	// ��ʾͼ�񴰿ڲ��ȴ�����
	cv::imshow("Polygon Drawing", image);
	cv::waitKey(0);

	return 0;
}
