cmake_minimum_required(VERSION 3.10)
project(edgeapp)

# set OpenCV path or use prebuilt OpenCV Android SDK
find_package(OpenCV REQUIRED)
add_library(native-lib SHARED native-lib.cpp)
target_include_directories(native-lib PRIVATE ${OpenCV_INCLUDE_DIRS})
target_link_libraries(native-lib ${OpenCV_LIBS})
