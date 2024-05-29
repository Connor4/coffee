cmake_minimum_required(VERSION 3.4.1)

add_library(serial_port SHARED src/main/cpp/SerialPort.c)
find_library(log-lib log)

target_link_libraries(serial_port ${log-lib})