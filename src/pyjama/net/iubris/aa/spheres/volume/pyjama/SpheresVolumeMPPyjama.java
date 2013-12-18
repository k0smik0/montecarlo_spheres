package net.iubris.aa.spheres.volume.pyjama;//####[1]####
import pt.runtime.*;//####[4]####
import pj.Pyjama;//####[5]####
import pj.PJPackageOnly;//####[6]####
import pj.UniqueThreadIdGeneratorForOpenMP;//####[7]####
import pi.ParIteratorFactory;//####[8]####
import pi.ParIterator;//####[9]####
import java.util.concurrent.atomic.*;//####[12]####
import java.util.concurrent.*;//####[13]####
import java.util.concurrent.CopyOnWriteArrayList;//####[21]####
import net.iubris.aa.spheres.volume.AbstractSpheresVolume;//####[22]####
import net.iubris.aa.spheres.model.Point;//####[23]####
import net.iubris.aa.spheres.model.Sphere;//####[24]####
import java.lang.reflect.*;//####[27]####
import java.util.concurrent.BlockingQueue;//####[27]####
import java.util.ArrayList;//####[27]####
//####[27]####
public class SpheresVolumeMPPyjama extends AbstractSpheresVolume {//####[29]####
    static{ParaTask.init();}//####[29]####
    /*  ParaTask helper method to access private/protected slots *///####[29]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[29]####
        if (m.getParameterTypes().length == 0)//####[29]####
            m.invoke(instance);//####[29]####
        else if ((m.getParameterTypes().length == 1))//####[29]####
            m.invoke(instance, arg);//####[29]####
        else //####[29]####
            m.invoke(instance, arg, interResult);//####[29]####
    }//####[29]####
//####[30]####
    public SpheresVolumeMPPyjama(String inFileName, double randomPointsUsed) {//####[30]####
        super(inFileName, randomPointsUsed);//####[31]####
        this.randomPointsFound = new CopyOnWriteArrayList<Point>();//####[32]####
    }//####[33]####
//####[34]####
    @Override//####[34]####
    public void calculate() {//####[34]####
        {//####[34]####
            checkPointsByPyjama();//####[35]####
            calculateVolume();//####[36]####
        }//####[37]####
    }//####[38]####
//####[39]####
    private static ArrayList<ParIterator<?>> _omp_piVarContainer = new ArrayList<ParIterator<?>>();//####[39]####
//####[40]####
    private static AtomicBoolean _holderForPIFirst = new AtomicBoolean(false);//####[40]####
//####[42]####
    protected void checkPointsByPyjama() {//####[42]####
        {//####[42]####
            int howSpheres = spheres.length;//####[43]####
            if (Pyjama.insideParallelRegion()) //####[45]####
            {//####[45]####
                {//####[47]####
                    for (int i = 0; i < randomPointsUsed; i = i + 1) //####[48]####
                    {//####[49]####
                        Point rp = randomPoints[i];//####[50]####
                        for (int j = 0; j < howSpheres; j++) //####[51]####
                        {//####[52]####
                            Sphere s = spheres[j];//####[53]####
                            if (s.contains(rp)) //####[54]####
                            {//####[55]####
                                randomPointsFoundedNumber++;//####[56]####
                                storeMoreInfo(s, rp);//####[57]####
                                break;//####[58]####
                            }//####[59]####
                        }//####[60]####
                    }//####[61]####
                }//####[62]####
            } else {//####[63]####
                PJPackageOnly.setThreadCountCurrentParallelRegion(Pyjama.omp_get_num_threads());//####[65]####
                _omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0 _omp__parallelRegionVarHolderInstance_0 = new _omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0();//####[68]####
                _omp__parallelRegionVarHolderInstance_0.howSpheres = howSpheres;//####[69]####
                PJPackageOnly.setMasterThread(Thread.currentThread());//####[72]####
                TaskID _omp__parallelRegionTaskID_0 = _ompParallelRegion_0(_omp__parallelRegionVarHolderInstance_0);//####[73]####
                __pt___ompParallelRegion_0(_omp__parallelRegionVarHolderInstance_0);//####[74]####
                try {//####[75]####
                    _omp__parallelRegionTaskID_0.waitTillFinished();//####[75]####
                } catch (Exception __pt__ex) {//####[75]####
                    __pt__ex.printStackTrace();//####[75]####
                }//####[75]####
                PJPackageOnly.setMasterThread(null);//####[77]####
                _holderForPIFirst.set(true);//####[78]####
                howSpheres = _omp__parallelRegionVarHolderInstance_0.howSpheres;//####[80]####
                PJPackageOnly.setThreadCountCurrentParallelRegion(1);//####[81]####
            }//####[82]####
        }//####[85]####
    }//####[86]####
//####[87]####
    private AtomicBoolean _imFirst_2 = new AtomicBoolean(true);//####[87]####
//####[88]####
    private AtomicInteger _numAssigned_2 = new AtomicInteger(0);//####[88]####
//####[89]####
    private AtomicInteger _imFinishedCounter_2 = new AtomicInteger(0);//####[89]####
//####[90]####
    private CountDownLatch _waitBarrier_2 = new CountDownLatch(1);//####[90]####
//####[91]####
    private CountDownLatch _waitBarrierAfter_2 = new CountDownLatch(1);//####[91]####
//####[92]####
    private ParIterator<Integer> _pi_2 = null;//####[92]####
//####[93]####
    private Integer _lastElement_2 = null;//####[93]####
//####[94]####
    private _ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables _ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables_instance = null;//####[94]####
//####[95]####
    private void _ompWorkSharedUserCode_SpheresVolumeMPPyjama2(_ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables __omp_vars) {//####[95]####
        int howSpheres = __omp_vars.howSpheres;//####[97]####
        Integer i;//####[98]####
        int __omp_strideAbs = 1 < 0 ? -(1) : (1);//####[99]####
        while (true) //####[100]####
        {//####[100]####
            int __omp_numBeenAssigned = _numAssigned_2.getAndAdd(__omp_strideAbs * 1);//####[101]####
            if (__omp_numBeenAssigned >= randomPointsUsed - (0)) //####[102]####
            break;//####[102]####
            int __omp_start = (0) + __omp_numBeenAssigned;//####[103]####
            double __omp_stop = __omp_start + __omp_strideAbs * 1 - 1;//####[104]####
            if (__omp_start > randomPointsUsed - __omp_strideAbs * 1) //####[105]####
            {//####[105]####
                __omp_stop = randomPointsUsed - 1;//####[105]####
            }//####[105]####
            for (i = __omp_start; i <= __omp_stop; i = i + __omp_strideAbs) //####[106]####
            {//####[108]####
                Point rp = randomPoints[i];//####[109]####
                for (int j = 0; j < howSpheres; j++) //####[110]####
                {//####[111]####
                    Sphere s = spheres[j];//####[112]####
                    if (s.contains(rp)) //####[113]####
                    {//####[114]####
                        randomPointsFoundedNumber++;//####[115]####
                        storeMoreInfo(s, rp);//####[116]####
                        break;//####[117]####
                    }//####[118]####
                }//####[119]####
            }//####[120]####
        }//####[121]####
        __omp_vars.howSpheres = howSpheres;//####[123]####
    }//####[124]####
//####[128]####
    private static volatile Method __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method = null;//####[128]####
    private synchronized static void __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_ensureMethodVarSet() {//####[128]####
        if (__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method == null) {//####[128]####
            try {//####[128]####
                __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt___ompParallelRegion_0", new Class[] {//####[128]####
                    _omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0.class//####[128]####
                });//####[128]####
            } catch (Exception e) {//####[128]####
                e.printStackTrace();//####[128]####
            }//####[128]####
        }//####[128]####
    }//####[128]####
    private TaskIDGroup<Void> _ompParallelRegion_0(_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0 __omp_vars) {//####[128]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[128]####
        return _ompParallelRegion_0(__omp_vars, new TaskInfo());//####[128]####
    }//####[128]####
    private TaskIDGroup<Void> _ompParallelRegion_0(_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0 __omp_vars, TaskInfo taskinfo) {//####[128]####
        // ensure Method variable is set//####[128]####
        if (__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method == null) {//####[128]####
            __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_ensureMethodVarSet();//####[128]####
        }//####[128]####
        taskinfo.setParameters(__omp_vars);//####[128]####
        taskinfo.setMethod(__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method);//####[128]####
        taskinfo.setInstance(this);//####[128]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Pyjama.omp_get_num_threads() - 1);//####[128]####
    }//####[128]####
    private TaskIDGroup<Void> _ompParallelRegion_0(TaskID<_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0> __omp_vars) {//####[128]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[128]####
        return _ompParallelRegion_0(__omp_vars, new TaskInfo());//####[128]####
    }//####[128]####
    private TaskIDGroup<Void> _ompParallelRegion_0(TaskID<_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0> __omp_vars, TaskInfo taskinfo) {//####[128]####
        // ensure Method variable is set//####[128]####
        if (__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method == null) {//####[128]####
            __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_ensureMethodVarSet();//####[128]####
        }//####[128]####
        taskinfo.setTaskIdArgIndexes(0);//####[128]####
        taskinfo.addDependsOn(__omp_vars);//####[128]####
        taskinfo.setParameters(__omp_vars);//####[128]####
        taskinfo.setMethod(__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method);//####[128]####
        taskinfo.setInstance(this);//####[128]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Pyjama.omp_get_num_threads() - 1);//####[128]####
    }//####[128]####
    private TaskIDGroup<Void> _ompParallelRegion_0(BlockingQueue<_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0> __omp_vars) {//####[128]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[128]####
        return _ompParallelRegion_0(__omp_vars, new TaskInfo());//####[128]####
    }//####[128]####
    private TaskIDGroup<Void> _ompParallelRegion_0(BlockingQueue<_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0> __omp_vars, TaskInfo taskinfo) {//####[128]####
        // ensure Method variable is set//####[128]####
        if (__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method == null) {//####[128]####
            __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_ensureMethodVarSet();//####[128]####
        }//####[128]####
        taskinfo.setQueueArgIndexes(0);//####[128]####
        taskinfo.setIsPipeline(true);//####[128]####
        taskinfo.setParameters(__omp_vars);//####[128]####
        taskinfo.setMethod(__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method);//####[128]####
        taskinfo.setInstance(this);//####[128]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Pyjama.omp_get_num_threads() - 1);//####[128]####
    }//####[128]####
    public void __pt___ompParallelRegion_0(_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0 __omp_vars) {//####[128]####
        int howSpheres = __omp_vars.howSpheres;//####[130]####
        {//####[131]####
            if (Pyjama.insideParallelRegion()) //####[132]####
            {//####[132]####
                boolean _omp_imFirst = _imFirst_2.getAndSet(false);//####[134]####
                _holderForPIFirst = _imFirst_2;//####[135]####
                if (_omp_imFirst) //####[136]####
                {//####[136]####
                    _ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables_instance = new _ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables();//####[137]####
                    int __omp_size_ = 0;//####[138]####
                    for (int i = 0; i < randomPointsUsed; i = i + 1) //####[140]####
                    {//####[140]####
                        _lastElement_2 = i;//####[141]####
                        __omp_size_++;//####[142]####
                    }//####[143]####
                    _pi_2 = ParIteratorFactory.createParIterator(0, __omp_size_, 1, Pyjama.omp_get_num_threads(), ParIterator.Schedule.DYNAMIC, ParIterator.DEFAULT_CHUNKSIZE, false);//####[144]####
                    _omp_piVarContainer.add(_pi_2);//####[145]####
                    _pi_2.setThreadIdGenerator(new UniqueThreadIdGeneratorForOpenMP());//####[146]####
                    _ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables_instance.howSpheres = howSpheres;//####[147]####
                    _waitBarrier_2.countDown();//####[148]####
                } else {//####[149]####
                    try {//####[150]####
                        _waitBarrier_2.await();//####[150]####
                    } catch (InterruptedException __omp__ie) {//####[150]####
                        __omp__ie.printStackTrace();//####[150]####
                    }//####[150]####
                }//####[151]####
                _ompWorkSharedUserCode_SpheresVolumeMPPyjama2(_ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables_instance);//####[152]####
                if (_imFinishedCounter_2.incrementAndGet() == PJPackageOnly.getThreadCountCurrentParallelRegion()) //####[153]####
                {//####[153]####
                    _waitBarrierAfter_2.countDown();//####[154]####
                } else {//####[155]####
                    try {//####[156]####
                        _waitBarrierAfter_2.await();//####[157]####
                    } catch (InterruptedException __omp__ie) {//####[158]####
                        __omp__ie.printStackTrace();//####[159]####
                    }//####[160]####
                }//####[161]####
            } else {//####[163]####
                for (int i = 0; i < randomPointsUsed; i = i + 1) //####[165]####
                {//####[166]####
                    Point rp = randomPoints[i];//####[167]####
                    for (int j = 0; j < __omp_vars.howSpheres; j++) //####[168]####
                    {//####[169]####
                        Sphere s = spheres[j];//####[170]####
                        if (s.contains(rp)) //####[171]####
                        {//####[172]####
                            randomPointsFoundedNumber++;//####[173]####
                            storeMoreInfo(s, rp);//####[174]####
                            break;//####[175]####
                        }//####[176]####
                    }//####[177]####
                }//####[178]####
            }//####[179]####
        }//####[181]####
        __omp_vars.howSpheres = howSpheres;//####[183]####
    }//####[184]####
//####[184]####
}//####[184]####
