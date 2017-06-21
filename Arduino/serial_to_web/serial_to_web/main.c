#include <errno.h>
#include <fcntl.h>
#include <string.h>
#include <termios.h>
#include <unistd.h>
#include <stdlib.h>
#include <regex.h>


regex_t regex;


struct _arduino_data{
    int hour;
    int currentWeatherId;
    int nextWeatherId;
};

typedef struct _arduino_data arduino_data;

int
set_interface_attribs (int fd, int speed, int parity)
{
    struct termios tty;
    memset (&tty, 0, sizeof tty);
    if (tcgetattr (fd, &tty) != 0)
    {
        printf ("error %d from tcgetattr", errno);
        return -1;
    }
    
    cfsetospeed (&tty, speed);
    cfsetispeed (&tty, speed);
    
    tty.c_cflag = (tty.c_cflag & ~CSIZE) | CS8;     // 8-bit chars
    // disable IGNBRK for mismatched speed tests; otherwise receive break
    // as \000 chars
    tty.c_iflag &= ~IGNBRK;         // disable break processing
    tty.c_lflag = 0;                // no signaling chars, no echo,
    // no canonical processing
    tty.c_oflag = 0;                // no remapping, no delays
    tty.c_cc[VMIN]  = 0;            // read doesn't block
    tty.c_cc[VTIME] = 5;            // 0.5 seconds read timeout
    
    tty.c_iflag &= ~(IXON | IXOFF | IXANY); // shut off xon/xoff ctrl
    
    tty.c_cflag |= (CLOCAL | CREAD);// ignore modem controls,
    // enable reading
    tty.c_cflag &= ~(PARENB | PARODD);      // shut off parity
    tty.c_cflag |= parity;
    tty.c_cflag &= ~CSTOPB;
    tty.c_cflag &= ~CRTSCTS;
    
    if (tcsetattr (fd, TCSANOW, &tty) != 0)
    {
        printf ("error %d from tcsetattr", errno);
        return -1;
    }
    return 0;
}
void
set_blocking (int fd, int should_block)
{
    struct termios tty;
    memset (&tty, 0, sizeof tty);
    if (tcgetattr (fd, &tty) != 0)
    {
        printf ("error %d from tggetattr", errno);
        return;
    }
    
    tty.c_cc[VMIN]  = should_block ? 1 : 0;
    tty.c_cc[VTIME] = 5;            // 0.5 seconds read timeout
    
    if (tcsetattr (fd, TCSANOW, &tty) != 0)
    printf ("error %d setting term attributes", errno);
}

int main(){
    int reti = regcomp(&regex, "@[0-9]{1,9};[0-9]{1,9};[0-9]{1,9};", REG_EXTENDED);
    if (reti) {
        printf("Could not compile regex\n");
        exit(1);
    }
    
    char *portname = "/dev/cu.usbmodem1411";
    
    int fd = open (portname, O_RDWR | O_NOCTTY | O_SYNC);
    if (fd < 0)
    {
        printf ("error %d opening %s: %s", errno, portname, strerror (errno));
        return 0;
    }
    
    set_interface_attribs (fd, B9600, 0);  // set speed to 115,200 bps, 8n1 (no parity)
    set_blocking (fd, 1);                // set blocking
    printf("Start !\n");
    while(1){
        regmatch_t rm[2];
        arduino_data ard;
        char buf[255];
        int n = read(fd,buf,255);
        int success = regexec(&regex, buf, 2, rm, 0);
        if(success == 0){
            char extractedWord[255];
            for(int i = rm[0].rm_so ; i < rm[0].rm_eo ; i++)
            {
                sprintf(extractedWord,"%c", buf[i]);
            }
            sscanf(extractedWord,"@%d;%d;%d;",&ard.hour,&ard.currentWeatherId,&ard.nextWeatherId);
            printf("\n");
            //sscanf(buf,"@%d;%d;%d;",&ard.hour,&ard.currentWeatherId,&ard.nextWeatherId);
        }
        else{
            printf("Error");
        }
    }
    regfree(&regex);
    usleep(100000);
}
