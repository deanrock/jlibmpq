# Define a variable for classpath
CLASS_PATH = ../bin

program_C_SRCS := $(wildcard *.c)
program_CXX_SRCS := $(wildcard *.cpp)
program_C_OBJS := ${program_C_SRCS:.c=.o}
program_CXX_OBJS := ${program_CXX_SRCS:.cpp=.o}
program_OBJS := $(program_C_OBJS) $(program_CXX_OBJS)
program_INCLUDE_DIRS :=
program_LIBRARY_DIRS :=
program_LIBRARIES :=

CPPFLAGS += $(foreach includedir,$(program_INCLUDE_DIRS),-I$(includedir))
LDFLAGS += $(foreach librarydir,$(program_LIBRARY_DIRS),-L$(librarydir))
LDFLAGS += $(foreach library,$(program_LIBRARIES),-l$(library)) -shared

# Define a virtual path for .class in the bin directory
vpath %.class $(CLASS_PATH)

all : libmpq.dll

# $@ matches the target, $< matches the first dependancy
libmpq.dll : $(program_OBJS)
	g++ -Wl,--add-stdcall-alias -static-libstdc++ -shared -o $@ $(program_OBJS)

%.o: %.cpp
	g++ -I"C:\Program Files\Java\jdk1.7.0_17\include" -I"C:\Program Files\Java\jdk1.7.0_17\include\win32" -c $< -o $@
 
# Applicable for create executable (without extension) from object .o object file
# $^ matches all the pre-requsities (no duplicates)
%: %.o
	ld $^ $(LOADLIBES) $(LDLIBS) -o $@

clean :
	rm *.o libmpq.dll
