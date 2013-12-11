


x = "`tail -1 sfere1.in| awk '{print $1}'`"
y = "`tail -1 sfere1.in| awk '{print $2}'`"
z = "`tail -1 sfere1.in| awk '{print $3}'`"
r = "`tail -1 sfere1.in| awk '{print $4}'`"

set title  "Montecarlo spheres "
set xlabel "x"
set ylabel "y"
set ylabel "z"

set parametric
set angle degree
set urange [0:360]
set vrange [-90:90]
set isosample 30,30

do for [i=1:1000] {
#	splot "<(sed -n 'ip' sfere1.in)" using 1:2 with lines
	x = "`sed -n 'i*1p' sfere1.in| awk '{print $1}'`"
	y = "`sed -n 'i*1p' sfere1.in| awk '{print $2}'`"
	z = "`sed -n 'i*1p' sfere1.in| awk '{print $3}'`"
	r = "`sed -n 'i*1p' sfere1.in| awk '{print $4}'`"

#	y = "`tail -1 isfere1.in| awk '{print $2}'`"
#	z = "`tail -1 sfere1.in| awk '{print $3}'`"
#	r = "`tail -1 sfere1.in| awk '{print $4}'`"
	splot r*cos(u)*cos(v)+x,r*sin(u)*cos(v)+y,r*sin(v)+z
}
#splot r*cos(u)*cos(v)+x,r*sin(u)*cos(v)+y,r*sin(v)+z
