#include <iostream>
#include <string>

extern "C"
{
#include <libavformat/avformat.h>
}

int main()
{
	avformat_network_init();

	AVFormatContext* formatContext = nullptr;
	if (avformat_open_input(&formatContext, "D:/input.mp4", nullptr, nullptr) != 0)
	{
		std::cerr << "Error opening input file" << std::endl;
		return 1;
	}

	if (avformat_find_stream_info(formatContext, nullptr) < 0)
	{
		std::cerr << "Error finding stream information" << std::endl;
		avformat_close_input(&formatContext);
		return 1;
	}

	int videoStreamIndex = -1;
	for (unsigned int i = 0; i < formatContext->nb_streams; ++i)
	{
		if (formatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO)
		{
			videoStreamIndex = i;
			break;
		}
	}

	if (videoStreamIndex == -1)
	{
		std::cerr << "No video stream found" << std::endl;
		avformat_close_input(&formatContext);
		return 1;
	}

	AVCodecParameters* videoCodecParams = formatContext->streams[videoStreamIndex]->codecpar;
	int frameWidth = videoCodecParams->width;
	int frameHeight = videoCodecParams->height;

	std::cout << "Video frame width: " << frameWidth << std::endl;
	std::cout << "Video frame height: " << frameHeight << std::endl;

	avformat_close_input(&formatContext);

	return 0;
}

