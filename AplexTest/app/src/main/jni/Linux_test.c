#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

#define BUZZER_ENABLE   182
#define BUZZER_FREQENCY 183
#define BUZZER_DISABLE  184

int main(void)
{
	int fd = open("/dev/buzzer", O_WRONLY);
	int ret = 0;
	if ( fd == -1 ) {
		perror("open buzzer device error.");
		exit(EXIT_FAILURE);
	}

    int frequency = 100;
	while ( frequency < 600 ) {
        frequency += 100;
        ioctl(fd, BUZZER_FREQENCY, frequency);
        ioctl(fd, BUZZER_ENABLE);
		sleep(1);
	}

    ioctl(fd, BUZZER_DISABLE);

	close(fd);
	return ret;
}
