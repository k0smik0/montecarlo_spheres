


x = "`tail -1 sphere1.dat| awk '{print $1}'`"
y = "`tail -1 sphere1.dat| awk '{print $2}'`"
z = "`tail -1 sphere1.dat| awk '{print $3}'`"
r = "`tail -1 sphere1.dat| awk '{print $4}'`"

set parametric
set angle degree
set urange [0:360]
set vrange [-90:90]
set isosample 30,30
splot r*cos(u)*cos(v)+x,r*sin(u)*cos(v)+y,r*sin(v)+z
