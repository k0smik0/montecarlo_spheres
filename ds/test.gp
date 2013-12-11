#definisce il terminale
#set term win

#definizione dei titoli per il grafico
set title "titolo del grfico"
set xlabel "Ascisse [u.m.]"
set ylabel "Ordinate [u.m.]" 


# grafica
pl [:][:] 'sfere1.in' u 1:2 t 'dat' w points
