#include <errno.h>
#include <fcntl.h>
#include <string.h>
#include <termios.h>
#include <unistd.h>
#include <stdlib.h>
#include <regex.h>
#include <stdio.h>
#include <stdbool.h>


regex_t regex;


struct _arduino_data{
    int hour;
    int currentWeatherId;
    int nextWeatherId;
};

typedef struct _arduino_data arduino_data;

#ifdef _WIN32
#ifndef _WIN32_WINNT
#define _WIN32_WINNT 0x0501
#endif
#include <winsock2.h>
#include <Ws2tcpip.h>
#else
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <unistd.h>
typedef int SOCKET;
#endif

int sockInit(void)
{
#ifdef _WIN32
    WSADATA wsa_data;
    return WSAStartup(MAKEWORD(1,1), &wsa_data);
#else
    return 0;
#endif
}

int sockQuit(void)
{
#ifdef _WIN32
    return WSACleanup();
#else
    return 0;
#endif
}

int sockClose(SOCKET sock)
{
    
    int status = 0;
    
#ifdef _WIN32
    status = shutdown(sock, SD_BOTH);
    if (status == 0) { status = closesocket(sock); }
#else
    status = shutdown(sock, SHUT_RDWR);
    if (status == 0) { status = close(sock); }
#endif
    
    return status;
    
}

#define ORAGE 0
#define PLUIE 1
#define NUAGE 2
#define SOLEIL 3
#define CANICULE 4


char * weatherNameFromId(int id){
    if(id == ORAGE)
        return "ORAGE";
    if(id == PLUIE)
        return "PLUIE";
    if(id == NUAGE)
        return "NUAGE";
    if(id == SOLEIL)
        return "SOLEIL";
    return "CANICULE";
}


char *generateJsonFromValues(arduino_data ard){
    char *generatedJson = malloc(1024);
    sprintf(generatedJson,
            "{"
                "\"timestamp\": %d,"
                "\"weather\": ["
                "{"
                    "\"dfn\": 0,"
                    "\"weather\": \"%s\""
                "},"
                "{"
                    "\"dfn\": 1,"
                    "\"weather\": \"%s\""
                "}"
                "]"
            "}", ard.hour, weatherNameFromId(ard.currentWeatherId), weatherNameFromId(ard.nextWeatherId));
    return generatedJson;
}

void updateMeteo(arduino_data ard) {
    char buffer[BUFSIZ];
    enum CONSTEXPR { MAX_REQUEST_LEN = 1024};
    char request[MAX_REQUEST_LEN];
    char *data = generateJsonFromValues(ard);
    char request_template[] = "POST /ValerianKang/Balady_API/1.0.0/meteorology HTTP/1.1\r\nHost: %s\r\nContent-type: application/json\r\nContent-length: %d\r\n\r\n%s\r\n";
    struct protoent *protoent;
    char *hostname = "localhost";
    in_addr_t in_addr;
    int request_len;
    int socket_file_descriptor;
    ssize_t nbytes_total, nbytes_last;
    struct hostent *hostent;
    struct sockaddr_in sockaddr_in;
    unsigned short server_port = 5000;
    
    request_len = snprintf(request, MAX_REQUEST_LEN, request_template, hostname, strlen(data), data);
    if (request_len >= MAX_REQUEST_LEN) {
        fprintf(stderr, "request length large: %d\n", request_len);
        exit(EXIT_FAILURE);
    }
    
    /* Build the socket. */
    protoent = getprotobyname("tcp");
    if (protoent == NULL) {
        perror("getprotobyname");
        exit(EXIT_FAILURE);
    }
    socket_file_descriptor = socket(AF_INET, SOCK_STREAM, protoent->p_proto);
    if (socket_file_descriptor == -1) {
        perror("socket");
        exit(EXIT_FAILURE);
    }
    
    /* Build the address. */
    hostent = gethostbyname(hostname);
    if (hostent == NULL) {
        fprintf(stderr, "error: gethostbyname(\"%s\")\n", hostname);
        exit(EXIT_FAILURE);
    }
    in_addr = inet_addr(inet_ntoa(*(struct in_addr*)*(hostent->h_addr_list)));
    if (in_addr == (in_addr_t)-1) {
        fprintf(stderr, "error: inet_addr(\"%s\")\n", *(hostent->h_addr_list));
        exit(EXIT_FAILURE);
    }
    sockaddr_in.sin_addr.s_addr = in_addr;
    sockaddr_in.sin_family = AF_INET;
    sockaddr_in.sin_port = htons(server_port);
    
    /* Actually connect. */
    if (connect(socket_file_descriptor, (struct sockaddr*)&sockaddr_in, sizeof(sockaddr_in)) == -1) {
        perror("connect");
        exit(EXIT_FAILURE);
    }
    
    /* Send HTTP request. */
    nbytes_total = 0;
    while (nbytes_total < request_len) {
        nbytes_last = write(socket_file_descriptor, request + nbytes_total, request_len - nbytes_total);
        if (nbytes_last == -1) {
            perror("write");
            exit(EXIT_FAILURE);
        }
        nbytes_total += nbytes_last;
    }
    while ((nbytes_total = read(socket_file_descriptor, buffer, BUFSIZ)) > 0) {
        //write(STDOUT_FILENO, buffer, nbytes_total);
    }
    if (nbytes_total == -1) {
        perror("read");
        exit(EXIT_FAILURE);
    }
    
    close(socket_file_descriptor);
}



arduino_data getCurrentState(int USB){
    arduino_data ard;
    int n  = 0;
    char buf = '\0';
    bool started = false;
    bool ended = false;
    char received[1024] = "";
    do {
        n = read( USB, &buf, 1 );
        if(buf == '@')
        {
            started = true;
            ended = false;
            memset(received, 0, 1024);
        }
        if(buf == '!' && started){
            ended = true;
        }
        sprintf(received, "%s%c\0", received, buf);
    } while( !started || !ended);
    sscanf(received, "@%d;%d;%d!", &ard.hour, &ard.currentWeatherId, &ard.nextWeatherId);
    return ard;
}


int main(){
    
    int USB = open( "/dev/cu.usbmodem1411", O_RDWR| O_NOCTTY );
    struct termios tty;
    struct termios tty_old;
    memset (&tty, 0, sizeof tty);
    
    /* Error Handling */
    if ( tcgetattr ( USB, &tty ) != 0 ) {
        printf("Error\n");
    }
    
    /* Save old tty parameters */
    tty_old = tty;
    
    /* Set Baud Rate */
    cfsetospeed (&tty, (speed_t)B9600);
    cfsetispeed (&tty, (speed_t)B9600);
    
    /* Setting other Port Stuff */
    tty.c_cflag     &=  ~PARENB;            // Make 8n1
    tty.c_cflag     &=  ~CSTOPB;
    tty.c_cflag     &=  ~CSIZE;
    tty.c_cflag     |=  CS8;
    
    tty.c_cflag     &=  ~CRTSCTS;           // no flow control
    tty.c_cc[VMIN]   =  1;                  // read doesn't block
    tty.c_cc[VTIME]  =  5;                  // 0.5 seconds read timeout
    tty.c_cflag     |=  CREAD | CLOCAL;     // turn on READ & ignore ctrl lines
    
    /* Make raw */
    cfmakeraw(&tty);
    
    /* Flush Port, then applies attributes */
    tcflush( USB, TCIFLUSH );
    if ( tcsetattr ( USB, TCSANOW, &tty ) != 0) {
        
        printf("Error");
    }

    
    
    printf("Start !\n");
    while(1){
        arduino_data ard = getCurrentState(USB);
        printf("Current hour of the game : %d\n", ard.hour );
        updateMeteo(ard);
        
    }
    
    printf("End\n");

    usleep(100000);
}
