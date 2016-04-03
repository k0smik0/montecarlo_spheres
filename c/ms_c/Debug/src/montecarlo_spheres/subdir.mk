################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/montecarlo_spheres/montecarlo_spheres_core.c 

OBJS += \
./src/montecarlo_spheres/montecarlo_spheres_core.o 

C_DEPS += \
./src/montecarlo_spheres/montecarlo_spheres_core.d 


# Each subdirectory must supply rules for building sources it contributes
src/montecarlo_spheres/%.o: ../src/montecarlo_spheres/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


