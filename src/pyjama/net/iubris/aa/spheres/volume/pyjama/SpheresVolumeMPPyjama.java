package net.iubris.aa.spheres.volume.pyjama;//####[1]####
//####[1]####
import pj.parser.ast.visitor.DummyClassToDetermineVariableType;//####[3]####
import pt.runtime.*;//####[4]####
import pj.Pyjama;//####[5]####
import pj.PJPackageOnly;//####[6]####
import pj.UniqueThreadIdGeneratorForOpenMP;//####[7]####
import pi.ParIteratorFactory;//####[8]####
import pi.ParIterator;//####[9]####
import pi.reductions.Reducible;//####[10]####
import pi.reductions.Reduction;//####[11]####
import java.util.concurrent.atomic.*;//####[12]####
import java.util.concurrent.*;//####[13]####
import java.awt.EventQueue;//####[14]####
import java.util.concurrent.ExecutorService;//####[15]####
import java.util.concurrent.Executors;//####[16]####
import java.util.concurrent.TimeUnit;//####[17]####
import javax.swing.SwingUtilities;//####[18]####
import pj.parser.ast.visitor.DummyClassToDetermineVariableType;//####[19]####
import java.util.LinkedList;//####[20]####
import java.util.concurrent.CopyOnWriteArrayList;//####[21]####
import net.iubris.aa.spheres.volume.AbstractSpheresVolume;//####[22]####
import net.iubris.aa.spheres.model.Point;//####[23]####
import net.iubris.aa.spheres.model.Sphere;//####[24]####
import pi.reductions.Reducible;//####[26]####
import java.util.*;//####[27]####
//####[27]####
//-- ParaTask related imports//####[27]####
import pt.runtime.*;//####[27]####
import java.util.concurrent.ExecutionException;//####[27]####
import java.util.concurrent.locks.*;//####[27]####
import java.lang.reflect.*;//####[27]####
import pt.runtime.GuiThread;//####[27]####
import java.util.concurrent.BlockingQueue;//####[27]####
import java.util.ArrayList;//####[27]####
import java.util.List;//####[27]####
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
                boolean _omp_b_isInEventLoop = EventQueue.isDispatchThread();//####[72]####
                if (true == _omp_b_isInEventLoop) //####[74]####
                {//####[74]####
                    try {//####[76]####
                        EventLoop.register();//####[77]####
                    } catch (Exception e) {//####[78]####
                    } finally {//####[79]####
                    }//####[79]####
                    TaskInfo __pt___omp__parallelRegionTaskID_0 = new TaskInfo();//####[80]####
//####[80]####
                    boolean isEDT = GuiThread.isEventDispatchThread();//####[80]####
//####[80]####
//####[80]####
                    /*  -- ParaTask notify clause for '_omp__parallelRegionTaskID_0' -- *///####[80]####
                    try {//####[80]####
                        Method __pt___omp__parallelRegionTaskID_0_slot_0 = null;//####[80]####
                        __pt___omp__parallelRegionTaskID_0_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "_ompNotifyMethod_0", new Class[] { TaskID.class });//####[81]####
                        TaskID __pt___omp__parallelRegionTaskID_0_slot_0_dummy_0 = null;//####[81]####
                        if (false) _ompNotifyMethod_0(__pt___omp__parallelRegionTaskID_0_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[81]####
                        __pt___omp__parallelRegionTaskID_0.addSlotToNotify(new Slot(__pt___omp__parallelRegionTaskID_0_slot_0, this, isEDT, false));//####[81]####
//####[81]####
                    } catch(Exception __pt__e) { //####[81]####
                        System.err.println("Problem registering method in clause:");//####[81]####
                        __pt__e.printStackTrace();//####[81]####
                    }//####[81]####
                    TaskID _omp__parallelRegionTaskID_0 = _ompParallelRegion_0(_omp__parallelRegionVarHolderInstance_0, __pt___omp__parallelRegionTaskID_0);//####[81]####
                    try {//####[82]####
                        EventLoop.exec();//####[83]####
                        EventLoop.quit();//####[84]####
                    } catch (Exception e) {//####[85]####
                    } finally {//####[86]####
                        return;//####[86]####
                    }//####[86]####
                } else {//####[88]####
                    PJPackageOnly.setMasterThread(Thread.currentThread());//####[90]####
                    TaskID _omp__parallelRegionTaskID_0 = _ompParallelRegion_0(_omp__parallelRegionVarHolderInstance_0);//####[91]####
                    __pt___ompParallelRegion_0(_omp__parallelRegionVarHolderInstance_0);//####[92]####
                    try {//####[93]####
                        _omp__parallelRegionTaskID_0.waitTillFinished();//####[93]####
                    } catch (Exception __pt__ex) {//####[93]####
                        __pt__ex.printStackTrace();//####[93]####
                    }//####[93]####
                    PJPackageOnly.setMasterThread(null);//####[95]####
                    _holderForPIFirst.set(true);//####[96]####
                    howSpheres = _omp__parallelRegionVarHolderInstance_0.howSpheres;//####[98]####
                    PJPackageOnly.setThreadCountCurrentParallelRegion(1);//####[99]####
                }//####[100]####
            }//####[101]####
        }//####[104]####
    }//####[105]####
//####[106]####
    private AtomicBoolean _imFirst_2 = new AtomicBoolean(true);//####[106]####
//####[107]####
    private AtomicInteger _numAssigned_2 = new AtomicInteger(0);//####[107]####
//####[108]####
    private AtomicInteger _imFinishedCounter_2 = new AtomicInteger(0);//####[108]####
//####[109]####
    private CountDownLatch _waitBarrier_2 = new CountDownLatch(1);//####[109]####
//####[110]####
    private CountDownLatch _waitBarrierAfter_2 = new CountDownLatch(1);//####[110]####
//####[111]####
    private ParIterator<Integer> _pi_2 = null;//####[111]####
//####[112]####
    private Integer _lastElement_2 = null;//####[112]####
//####[113]####
    private _ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables _ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables_instance = null;//####[113]####
//####[114]####
    private void _ompWorkSharedUserCode_SpheresVolumeMPPyjama2(_ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables __omp_vars) {//####[114]####
        int howSpheres = __omp_vars.howSpheres;//####[116]####
        Integer i;//####[117]####
        int __omp_strideAbs = 1 < 0 ? -(1) : (1);//####[118]####
        while (true) //####[119]####
        {//####[119]####
            int __omp_numBeenAssigned = _numAssigned_2.getAndAdd(__omp_strideAbs * 1);//####[120]####
            if (__omp_numBeenAssigned >= randomPointsUsed - (0)) //####[121]####
            break;//####[121]####
            int __omp_start = (0) + __omp_numBeenAssigned;//####[122]####
            double __omp_stop = __omp_start + __omp_strideAbs * 1 - 1;//####[123]####
            if (__omp_start > randomPointsUsed - __omp_strideAbs * 1) //####[124]####
            {//####[124]####
                __omp_stop = randomPointsUsed - 1;//####[124]####
            }//####[124]####
            for (i = __omp_start; i <= __omp_stop; i = i + __omp_strideAbs) //####[125]####
            {//####[127]####
                Point rp = randomPoints[i];//####[128]####
                for (int j = 0; j < howSpheres; j++) //####[129]####
                {//####[130]####
                    Sphere s = spheres[j];//####[131]####
                    if (s.contains(rp)) //####[132]####
                    {//####[133]####
                        randomPointsFoundedNumber++;//####[134]####
                        storeMoreInfo(s, rp);//####[135]####
                        break;//####[136]####
                    }//####[137]####
                }//####[138]####
            }//####[139]####
        }//####[140]####
        __omp_vars.howSpheres = howSpheres;//####[142]####
    }//####[143]####
//####[147]####
    private static volatile Method __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method = null;//####[147]####
    private synchronized static void __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_ensureMethodVarSet() {//####[147]####
        if (__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method == null) {//####[147]####
            try {//####[147]####
                __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt___ompParallelRegion_0", new Class[] {//####[147]####
                    _omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0.class//####[147]####
                });//####[147]####
            } catch (Exception e) {//####[147]####
                e.printStackTrace();//####[147]####
            }//####[147]####
        }//####[147]####
    }//####[147]####
    private TaskIDGroup<Void> _ompParallelRegion_0(_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0 __omp_vars) {//####[147]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[147]####
        return _ompParallelRegion_0(__omp_vars, new TaskInfo());//####[147]####
    }//####[147]####
    private TaskIDGroup<Void> _ompParallelRegion_0(_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0 __omp_vars, TaskInfo taskinfo) {//####[147]####
        // ensure Method variable is set//####[147]####
        if (__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method == null) {//####[147]####
            __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_ensureMethodVarSet();//####[147]####
        }//####[147]####
        taskinfo.setParameters(__omp_vars);//####[147]####
        taskinfo.setMethod(__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method);//####[147]####
        taskinfo.setInstance(this);//####[147]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Pyjama.omp_get_num_threads() - 1);//####[147]####
    }//####[147]####
    private TaskIDGroup<Void> _ompParallelRegion_0(TaskID<_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0> __omp_vars) {//####[147]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[147]####
        return _ompParallelRegion_0(__omp_vars, new TaskInfo());//####[147]####
    }//####[147]####
    private TaskIDGroup<Void> _ompParallelRegion_0(TaskID<_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0> __omp_vars, TaskInfo taskinfo) {//####[147]####
        // ensure Method variable is set//####[147]####
        if (__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method == null) {//####[147]####
            __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_ensureMethodVarSet();//####[147]####
        }//####[147]####
        taskinfo.setTaskIdArgIndexes(0);//####[147]####
        taskinfo.addDependsOn(__omp_vars);//####[147]####
        taskinfo.setParameters(__omp_vars);//####[147]####
        taskinfo.setMethod(__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method);//####[147]####
        taskinfo.setInstance(this);//####[147]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Pyjama.omp_get_num_threads() - 1);//####[147]####
    }//####[147]####
    private TaskIDGroup<Void> _ompParallelRegion_0(BlockingQueue<_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0> __omp_vars) {//####[147]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[147]####
        return _ompParallelRegion_0(__omp_vars, new TaskInfo());//####[147]####
    }//####[147]####
    private TaskIDGroup<Void> _ompParallelRegion_0(BlockingQueue<_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0> __omp_vars, TaskInfo taskinfo) {//####[147]####
        // ensure Method variable is set//####[147]####
        if (__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method == null) {//####[147]####
            __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_ensureMethodVarSet();//####[147]####
        }//####[147]####
        taskinfo.setQueueArgIndexes(0);//####[147]####
        taskinfo.setIsPipeline(true);//####[147]####
        taskinfo.setParameters(__omp_vars);//####[147]####
        taskinfo.setMethod(__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0_method);//####[147]####
        taskinfo.setInstance(this);//####[147]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Pyjama.omp_get_num_threads() - 1);//####[147]####
    }//####[147]####
    public void __pt___ompParallelRegion_0(_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0 __omp_vars) {//####[147]####
        int howSpheres = __omp_vars.howSpheres;//####[149]####
        {//####[150]####
            if (Pyjama.insideParallelRegion()) //####[151]####
            {//####[151]####
                boolean _omp_imFirst = _imFirst_2.getAndSet(false);//####[153]####
                _holderForPIFirst = _imFirst_2;//####[154]####
                if (_omp_imFirst) //####[155]####
                {//####[155]####
                    _ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables_instance = new _ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables();//####[156]####
                    int __omp_size_ = 0;//####[157]####
                    for (int i = 0; i < randomPointsUsed; i = i + 1) //####[159]####
                    {//####[159]####
                        _lastElement_2 = i;//####[160]####
                        __omp_size_++;//####[161]####
                    }//####[162]####
                    _pi_2 = ParIteratorFactory.createParIterator(0, __omp_size_, 1, Pyjama.omp_get_num_threads(), ParIterator.Schedule.DYNAMIC, ParIterator.DEFAULT_CHUNKSIZE, false);//####[163]####
                    _omp_piVarContainer.add(_pi_2);//####[164]####
                    _pi_2.setThreadIdGenerator(new UniqueThreadIdGeneratorForOpenMP());//####[165]####
                    _ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables_instance.howSpheres = howSpheres;//####[166]####
                    _waitBarrier_2.countDown();//####[167]####
                } else {//####[168]####
                    try {//####[169]####
                        _waitBarrier_2.await();//####[169]####
                    } catch (InterruptedException __omp__ie) {//####[169]####
                        __omp__ie.printStackTrace();//####[169]####
                    }//####[169]####
                }//####[170]####
                _ompWorkSharedUserCode_SpheresVolumeMPPyjama2(_ompWorkSharedUserCode_SpheresVolumeMPPyjama2_variables_instance);//####[171]####
                if (_imFinishedCounter_2.incrementAndGet() == PJPackageOnly.getThreadCountCurrentParallelRegion()) //####[172]####
                {//####[172]####
                    _waitBarrierAfter_2.countDown();//####[173]####
                } else {//####[174]####
                    try {//####[175]####
                        _waitBarrierAfter_2.await();//####[176]####
                    } catch (InterruptedException __omp__ie) {//####[177]####
                        __omp__ie.printStackTrace();//####[178]####
                    }//####[179]####
                }//####[180]####
            } else {//####[182]####
                for (int i = 0; i < randomPointsUsed; i = i + 1) //####[184]####
                {//####[185]####
                    Point rp = randomPoints[i];//####[186]####
                    for (int j = 0; j < __omp_vars.howSpheres; j++) //####[187]####
                    {//####[188]####
                        Sphere s = spheres[j];//####[189]####
                        if (s.contains(rp)) //####[190]####
                        {//####[191]####
                            randomPointsFoundedNumber++;//####[192]####
                            storeMoreInfo(s, rp);//####[193]####
                            break;//####[194]####
                        }//####[195]####
                    }//####[196]####
                }//####[197]####
            }//####[198]####
        }//####[200]####
        __omp_vars.howSpheres = howSpheres;//####[202]####
    }//####[203]####
//####[203]####
//####[204]####
    protected void _ompNotifyMethod_0(TaskID id) {//####[204]####
        {//####[204]####
            _omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0 __omp_vars = (_omp__parallelRegionVarHolderClass_SpheresVolumeMPPyjama0) id.getTaskArguments()[0];//####[205]####
            int howSpheres = __omp_vars.howSpheres;//####[206]####
            PJPackageOnly.setThreadCountCurrentParallelRegion(1);//####[207]####
            _holderForPIFirst.set(true);//####[208]####
        }//####[209]####
    }//####[210]####
}//####[210]####
