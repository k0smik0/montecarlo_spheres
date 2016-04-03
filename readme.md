### montecarlo spheres

An experiment with some solutions for task parallel computing: omp, mpi, jomp, pyjama, jcuda

Applying a simple montecarlo algorithm, this tool computes resulting volume on a set of spheres (hence the name) in various position (intersecting, tangenting, concentric).

The C version is from an academic test on omp api and mpi protocols, where only numeric result was requested.

The Java version is my personal experiment, and it would compare various libraries for parallel programming in Java: jomp, pyjama, jcuda. They provides all omp-like syntax, using directive and so on, and experiment goal is to achieve some benchmarks for this solutions. For jCuda, you must have a nVidia video cards.
As pleasant bonus, the java version provides a visualization on a 3D cartesian plot using j3d ;D

##### update: from Java8 we have functional paradigm and a new api for task-parallel, applied on functional syntax (the 'Stream'), so jomp and pyjama could be considered as obsolete/useless, but you never know ;D
	#### For jCuda, it's a different situation: it parallelize computation on video card (if found), exploiting nvidia pipelines, or multicore environment (if found) in a transparent way - à la omp, but in a more powerful manner, isn't?


Run it, and enjoy yourself ;D

Below, images from java computations:

<div> <span>
<img alt="1000_random_spheres" src="https://raw.githubusercontent.com/k0smik0/montecarlo_spheres/master/java/montecarlo_spheres_java/images/1000_sfere_casuali_3d.png" 
height="200" > 
</span><span>
<img alt="1000_random_spheres_only_points" src="https://raw.githubusercontent.com/k0smik0/montecarlo_spheres/master/java/montecarlo_spheres_java/images/1000_sfere_casuali_3d_only_found_points.png" height="200"> 
</span></div>

<div> <span>
<img alt="1000_concentric_spheres" src="https://raw.githubusercontent.com/k0smik0/montecarlo_spheres/master/java/montecarlo_spheres_java/images/1000_sfere_concentriche_3d.png" height="200" >
</span><span>
<img alt="1000_concentric_spheres_only_points" src="https://raw.githubusercontent.com/k0smik0/montecarlo_spheres/master/java/montecarlo_spheres_java/images/1000_sfere_concentriche_3d_only_found_points.png" height="200" >
</span></div>

<div> <span>
<img alt="1000_not_intersecting_nor_tangent_spheres" src="https://raw.githubusercontent.com/k0smik0/montecarlo_spheres/master/java/montecarlo_spheres_java/images/1000_sfere_non_intersecanti_ne_tangenti_3d.png" height="200" >
</span><span>
<img alt="1000_not_intersecting_nor_tangent_spheres_only_points" src="https://raw.githubusercontent.com/k0smik0/montecarlo_spheres/master/java/montecarlo_spheres_java/images/1000_sfere_non_intersecanti_ne_tangenti_3d_only_found_points.png" height="200" >
</span></div>

<div> <span>
<img alt="other_spheres" src="https://raw.githubusercontent.com/k0smik0/montecarlo_spheres/master/java/montecarlo_spheres_java/images/altre_sfere_3d.png" height="200">
</span><span>
<img alt="other_spheres_only_points" src="https://raw.githubusercontent.com/k0smik0/montecarlo_spheres/master/java/montecarlo_spheres_java/images/altre_sfere_3d_only_found_points.png" 
height="200" >
</span></div>
