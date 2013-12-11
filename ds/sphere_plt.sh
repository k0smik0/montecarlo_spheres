#!/bin/bash

#x = "`tail -1 sfere1.in| awk '{print $1}'`"
#y = "`tail -1 sfere1.in| awk '{print $2}'`"
#z = "`tail -1 sfere1.in| awk '{print $3}'`"
#r = "`tail -1 sfere1.in| awk '{print $4}'`"

fileIn=$1
fileOut="$( basename ${fileIn} in )plt"

cat << 'EF' > ${fileOut}
set title  "Montecarlo spheres "
set xlabel "x"
set ylabel "y"
set ylabel "z"

set parametric
set angle degree
set urange [0:360]
set vrange [-90:90]
set isosample 30,30


EF

for i in $(seq 1 1000)
do
	x=$(sed -n "${i}p" $fileIn| awk '{print $1}')
	y=$(sed -n "${i}p" $fileIn| awk '{print $2}')
	z=$(sed -n "${i}p" $fileIn| awk '{print $3}')
	radius=$(sed -n "${i}p" $fileIn| awk '{print $4}')

#	echo "$x $y $z $radius"
	
#	gnuplot <<- EOF
#echo "splot $radius*cos(u)*cos(v)+$x,$radius*sin(u)*cos(v)+$y,$radius*sin(v)+$z" | gnuplot
	echo "splot $radius*cos(u)*cos(v)+$x,$radius*sin(u)*cos(v)+$y,$radius*sin(v)+$z" >> ${fileOut}
	echo -n "$i "

#EOF
done
echo .

#do for [i=1:1000] {
#	splot "<(sed -n 'ip' sfere1.in)" using 1:2 with lines#
#	x = "`sed -n 'i*1p' sfere1.in| awk '{print $1}'`"#
#	y = "`sed -n 'i*1p' sfere1.in| awk '{print $2}'`"
#	z = "`sed -n 'i*1p' sfere1.in| awk '{print $3}'`"
#	r = "`sed -n 'i*1p' sfere1.in| awk '{print $4}'`"

#	plot x; 
#	splot r*cos(u)*cos(v)+x,r*sin(u)*cos(v)+y,r*sin(v)+z
#}

