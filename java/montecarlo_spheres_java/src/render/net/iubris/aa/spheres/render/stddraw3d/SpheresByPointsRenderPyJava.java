package net.iubris.aa.spheres.render.stddraw3d;//####[1]####
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
import java.awt.Color;//####[20]####
import javax.media.j3d.RestrictedAccessException;//####[21]####
import net.iubris.aa.spheres.model.Point;//####[22]####
import net.iubris.aa.spheres.volume.AbstractSpheresVolume;//####[23]####
import edu.princeton.stddraw3d.StdDraw3D;//####[24]####
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
public class SpheresByPointsRenderPyJava extends AbstractRender {//####[29]####
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
    public SpheresByPointsRenderPyJava(AbstractSpheresVolume spheresVolume) {//####[30]####
        super(spheresVolume);//####[31]####
    }//####[32]####
//####[32]####
    public void draw() {//####[32]####
        {//####[32]####
            StdDraw3D.setCanvasSize(1280, 800);//####[33]####
            drawBoundingBox();//####[34]####
            drawAxis();//####[35]####
            drawSpheresByPoints();//####[36]####
            StdDraw3D.finished();//####[37]####
        }//####[38]####
    }//####[39]####
//####[40]####
    private static ArrayList<ParIterator<?>> _omp_piVarContainer = new ArrayList<ParIterator<?>>();//####[40]####
//####[41]####
    private static AtomicBoolean _holderForPIFirst = new AtomicBoolean(false);//####[41]####
//####[43]####
    private void drawSpheresByPoints() {//####[43]####
        {//####[43]####
            Point[] randomPoints = spheresVolume.getRandomPointsFound().toArray(new Point[0]);//####[44]####
            int randomPointsHow = randomPoints.length;//####[45]####
            double[] rxs = new double[randomPointsHow];//####[46]####
            double[] rys = new double[randomPointsHow];//####[47]####
            double[] rzs = new double[randomPointsHow];//####[48]####
            System.out.println("randomPointsHow: " + randomPointsHow);//####[49]####
            int found = 0;//####[50]####
            int notFound = 0;//####[51]####
            if (Pyjama.insideParallelRegion()) //####[53]####
            {//####[53]####
                {//####[55]####
                    for (int i = 0; i < randomPointsHow; i = i + 1) //####[56]####
                    {//####[57]####
                        try {//####[58]####
                            Point randomPoint = randomPoints[i];//####[59]####
                            rxs[i] = randomPoint.getX();//####[60]####
                            rys[i] = randomPoint.getY();//####[61]####
                            rzs[i] = randomPoint.getZ();//####[62]####
                            found++;//####[63]####
                        } catch (NullPointerException e) {//####[64]####
                            System.out.println("e:" + i);//####[65]####
                            ;//####[66]####
                            notFound++;//####[67]####
                        }//####[68]####
                    }//####[69]####
                }//####[70]####
            } else {//####[71]####
                PJPackageOnly.setThreadCountCurrentParallelRegion(Pyjama.omp_get_num_threads());//####[73]####
                _omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0 _omp__parallelRegionVarHolderInstance_0 = new _omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0();//####[76]####
                _omp__parallelRegionVarHolderInstance_0.rys = rys;//####[77]####
                _omp__parallelRegionVarHolderInstance_0.rzs = rzs;//####[78]####
                _omp__parallelRegionVarHolderInstance_0.randomPoints = randomPoints;//####[79]####
                _omp__parallelRegionVarHolderInstance_0.randomPointsHow = randomPointsHow;//####[80]####
                _omp__parallelRegionVarHolderInstance_0.notFound = notFound;//####[81]####
                _omp__parallelRegionVarHolderInstance_0.rxs = rxs;//####[82]####
                _omp__parallelRegionVarHolderInstance_0.found = found;//####[83]####
                boolean _omp_b_isInEventLoop = EventQueue.isDispatchThread();//####[86]####
                if (true == _omp_b_isInEventLoop) //####[88]####
                {//####[88]####
                    try {//####[90]####
                        EventLoop.register();//####[91]####
                    } catch (Exception e) {//####[92]####
                    } finally {//####[93]####
                    }//####[93]####
                    TaskInfo __pt___omp__parallelRegionTaskID_0 = new TaskInfo();//####[94]####
//####[94]####
                    boolean isEDT = GuiThread.isEventDispatchThread();//####[94]####
//####[94]####
//####[94]####
                    /*  -- ParaTask notify clause for '_omp__parallelRegionTaskID_0' -- *///####[94]####
                    try {//####[94]####
                        Method __pt___omp__parallelRegionTaskID_0_slot_0 = null;//####[94]####
                        __pt___omp__parallelRegionTaskID_0_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "_ompNotifyMethod_0", new Class[] { TaskID.class });//####[95]####
                        TaskID __pt___omp__parallelRegionTaskID_0_slot_0_dummy_0 = null;//####[95]####
                        if (false) _ompNotifyMethod_0(__pt___omp__parallelRegionTaskID_0_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[95]####
                        __pt___omp__parallelRegionTaskID_0.addSlotToNotify(new Slot(__pt___omp__parallelRegionTaskID_0_slot_0, this, isEDT, false));//####[95]####
//####[95]####
                    } catch(Exception __pt__e) { //####[95]####
                        System.err.println("Problem registering method in clause:");//####[95]####
                        __pt__e.printStackTrace();//####[95]####
                    }//####[95]####
                    TaskID _omp__parallelRegionTaskID_0 = _ompParallelRegion_0(_omp__parallelRegionVarHolderInstance_0, __pt___omp__parallelRegionTaskID_0);//####[95]####
                    try {//####[96]####
                        EventLoop.exec();//####[97]####
                        EventLoop.quit();//####[98]####
                    } catch (Exception e) {//####[99]####
                    } finally {//####[100]####
                        return;//####[100]####
                    }//####[100]####
                } else {//####[102]####
                    PJPackageOnly.setMasterThread(Thread.currentThread());//####[104]####
                    TaskID _omp__parallelRegionTaskID_0 = _ompParallelRegion_0(_omp__parallelRegionVarHolderInstance_0);//####[105]####
                    __pt___ompParallelRegion_0(_omp__parallelRegionVarHolderInstance_0);//####[106]####
                    try {//####[107]####
                        _omp__parallelRegionTaskID_0.waitTillFinished();//####[107]####
                    } catch (Exception __pt__ex) {//####[107]####
                        __pt__ex.printStackTrace();//####[107]####
                    }//####[107]####
                    PJPackageOnly.setMasterThread(null);//####[109]####
                    _holderForPIFirst.set(true);//####[110]####
                    rys = _omp__parallelRegionVarHolderInstance_0.rys;//####[112]####
                    rzs = _omp__parallelRegionVarHolderInstance_0.rzs;//####[113]####
                    randomPoints = _omp__parallelRegionVarHolderInstance_0.randomPoints;//####[114]####
                    randomPointsHow = _omp__parallelRegionVarHolderInstance_0.randomPointsHow;//####[115]####
                    notFound = _omp__parallelRegionVarHolderInstance_0.notFound;//####[116]####
                    rxs = _omp__parallelRegionVarHolderInstance_0.rxs;//####[117]####
                    found = _omp__parallelRegionVarHolderInstance_0.found;//####[118]####
                    PJPackageOnly.setThreadCountCurrentParallelRegion(1);//####[119]####
                }//####[120]####
            }//####[121]####
            System.out.println("found: " + found);//####[124]####
            System.out.println("notFound: " + notFound);//####[125]####
            {//####[126]####
                StdDraw3D.points(rxs, rys, rzs).setColor(Color.GREEN, 80);//####[127]####
            }//####[128]####
            boolean _omp_b_isInEventLoop = EventQueue.isDispatchThread();//####[129]####
            if (false == _omp_b_isInEventLoop) //####[131]####
            {//####[131]####
                final double[] _omp_gui_4rys = rys;//####[133]####
                final double[] _omp_gui_4rzs = rzs;//####[134]####
                final Point[] _omp_gui_4randomPoints = randomPoints;//####[135]####
                final int _omp_gui_4randomPointsHow = randomPointsHow;//####[136]####
                final int _omp_gui_4notFound = notFound;//####[137]####
                final double[] _omp_gui_4rxs = rxs;//####[138]####
                final int _omp_gui_4found = found;//####[139]####
                try {//####[141]####
                    SwingUtilities.invokeLater(new Runnable() {//####[141]####
//####[143]####
                        public void run() {//####[144]####
                            StdDraw3D.points(_omp_gui_4rxs, _omp_gui_4rys, _omp_gui_4rzs).setColor(Color.GREEN, 80);//####[145]####
                        }//####[146]####
                    });//####[146]####
                } catch (Exception e) {//####[148]####
                    e.printStackTrace();//####[149]####
                }//####[150]####
            } else {//####[151]####
                {//####[153]####
                    StdDraw3D.points(rxs, rys, rzs).setColor(Color.GREEN, 80);//####[154]####
                }//####[155]####
            }//####[156]####
        }//####[158]####
    }//####[159]####
//####[160]####
    private AtomicBoolean _imFirst_2 = new AtomicBoolean(true);//####[160]####
//####[161]####
    private AtomicInteger _numAssigned_2 = new AtomicInteger(0);//####[161]####
//####[162]####
    private AtomicInteger _imFinishedCounter_2 = new AtomicInteger(0);//####[162]####
//####[163]####
    private CountDownLatch _waitBarrier_2 = new CountDownLatch(1);//####[163]####
//####[164]####
    private CountDownLatch _waitBarrierAfter_2 = new CountDownLatch(1);//####[164]####
//####[165]####
    private ParIterator<Integer> _pi_2 = null;//####[165]####
//####[166]####
    private Integer _lastElement_2 = null;//####[166]####
//####[167]####
    private _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance = null;//####[167]####
//####[168]####
    private void _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2(_ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables __omp_vars) {//####[168]####
        int found = __omp_vars.found[Pyjama.omp_get_thread_num()];//####[170]####
        int notFound = __omp_vars.notFound[Pyjama.omp_get_thread_num()];//####[171]####
        double[] rys = __omp_vars.rys;//####[172]####
        double[] rzs = __omp_vars.rzs;//####[173]####
        Point[] randomPoints = __omp_vars.randomPoints;//####[174]####
        int randomPointsHow = __omp_vars.randomPointsHow;//####[175]####
        double[] rxs = __omp_vars.rxs;//####[176]####
        Integer i;//####[177]####
        int __omp_strideAbs = 1 < 0 ? -(1) : (1);//####[178]####
        while (true) //####[179]####
        {//####[179]####
            int __omp_numBeenAssigned = _numAssigned_2.getAndAdd(__omp_strideAbs * 1);//####[180]####
            if (__omp_numBeenAssigned >= randomPointsHow - (0)) //####[181]####
            break;//####[181]####
            int __omp_start = (0) + __omp_numBeenAssigned;//####[182]####
            int __omp_stop = __omp_start + __omp_strideAbs * 1 - 1;//####[183]####
            if (__omp_start > randomPointsHow - __omp_strideAbs * 1) //####[184]####
            {//####[184]####
                __omp_stop = randomPointsHow - 1;//####[184]####
            }//####[184]####
            for (i = __omp_start; i <= __omp_stop; i = i + __omp_strideAbs) //####[185]####
            {//####[187]####
                try {//####[188]####
                    Point randomPoint = randomPoints[i];//####[189]####
                    rxs[i] = randomPoint.getX();//####[190]####
                    rys[i] = randomPoint.getY();//####[191]####
                    rzs[i] = randomPoint.getZ();//####[192]####
                    found++;//####[193]####
                } catch (NullPointerException e) {//####[194]####
                    System.out.println("e:" + i);//####[195]####
                    ;//####[196]####
                    notFound++;//####[197]####
                }//####[198]####
            }//####[199]####
        }//####[200]####
        __omp_vars.reducible_found.set(found);//####[202]####
        __omp_vars.reducible_notFound.set(notFound);//####[203]####
        __omp_vars.rys = rys;//####[204]####
        __omp_vars.rzs = rzs;//####[205]####
        __omp_vars.randomPoints = randomPoints;//####[206]####
        __omp_vars.randomPointsHow = randomPointsHow;//####[207]####
        __omp_vars.rxs = rxs;//####[208]####
    }//####[209]####
//####[213]####
    private static volatile Method __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0_method = null;//####[213]####
    private synchronized static void __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0_ensureMethodVarSet() {//####[213]####
        if (__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0_method == null) {//####[213]####
            try {//####[213]####
                __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt___ompParallelRegion_0", new Class[] {//####[213]####
                    _omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0.class//####[213]####
                });//####[213]####
            } catch (Exception e) {//####[213]####
                e.printStackTrace();//####[213]####
            }//####[213]####
        }//####[213]####
    }//####[213]####
    private TaskIDGroup<Void> _ompParallelRegion_0(_omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0 __omp_vars) {//####[213]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[213]####
        return _ompParallelRegion_0(__omp_vars, new TaskInfo());//####[213]####
    }//####[213]####
    private TaskIDGroup<Void> _ompParallelRegion_0(_omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0 __omp_vars, TaskInfo taskinfo) {//####[213]####
        // ensure Method variable is set//####[213]####
        if (__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0_method == null) {//####[213]####
            __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0_ensureMethodVarSet();//####[213]####
        }//####[213]####
        taskinfo.setParameters(__omp_vars);//####[213]####
        taskinfo.setMethod(__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0_method);//####[213]####
        taskinfo.setInstance(this);//####[213]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Pyjama.omp_get_num_threads() - 1);//####[213]####
    }//####[213]####
    private TaskIDGroup<Void> _ompParallelRegion_0(TaskID<_omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0> __omp_vars) {//####[213]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[213]####
        return _ompParallelRegion_0(__omp_vars, new TaskInfo());//####[213]####
    }//####[213]####
    private TaskIDGroup<Void> _ompParallelRegion_0(TaskID<_omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0> __omp_vars, TaskInfo taskinfo) {//####[213]####
        // ensure Method variable is set//####[213]####
        if (__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0_method == null) {//####[213]####
            __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0_ensureMethodVarSet();//####[213]####
        }//####[213]####
        taskinfo.setTaskIdArgIndexes(0);//####[213]####
        taskinfo.addDependsOn(__omp_vars);//####[213]####
        taskinfo.setParameters(__omp_vars);//####[213]####
        taskinfo.setMethod(__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0_method);//####[213]####
        taskinfo.setInstance(this);//####[213]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Pyjama.omp_get_num_threads() - 1);//####[213]####
    }//####[213]####
    private TaskIDGroup<Void> _ompParallelRegion_0(BlockingQueue<_omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0> __omp_vars) {//####[213]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[213]####
        return _ompParallelRegion_0(__omp_vars, new TaskInfo());//####[213]####
    }//####[213]####
    private TaskIDGroup<Void> _ompParallelRegion_0(BlockingQueue<_omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0> __omp_vars, TaskInfo taskinfo) {//####[213]####
        // ensure Method variable is set//####[213]####
        if (__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0_method == null) {//####[213]####
            __pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0_ensureMethodVarSet();//####[213]####
        }//####[213]####
        taskinfo.setQueueArgIndexes(0);//####[213]####
        taskinfo.setIsPipeline(true);//####[213]####
        taskinfo.setParameters(__omp_vars);//####[213]####
        taskinfo.setMethod(__pt___ompParallelRegion_0__omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0_method);//####[213]####
        taskinfo.setInstance(this);//####[213]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Pyjama.omp_get_num_threads() - 1);//####[213]####
    }//####[213]####
    public void __pt___ompParallelRegion_0(_omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0 __omp_vars) {//####[213]####
        double[] rys = __omp_vars.rys;//####[215]####
        double[] rzs = __omp_vars.rzs;//####[216]####
        Point[] randomPoints = __omp_vars.randomPoints;//####[217]####
        int randomPointsHow = __omp_vars.randomPointsHow;//####[218]####
        int notFound = __omp_vars.notFound;//####[219]####
        double[] rxs = __omp_vars.rxs;//####[220]####
        int found = __omp_vars.found;//####[221]####
        {//####[222]####
            if (Pyjama.insideParallelRegion()) //####[223]####
            {//####[223]####
                boolean _omp_imFirst = _imFirst_2.getAndSet(false);//####[225]####
                _holderForPIFirst = _imFirst_2;//####[226]####
                if (_omp_imFirst) //####[227]####
                {//####[227]####
                    _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance = new _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables();//####[228]####
                    int __omp_size_ = 0;//####[229]####
                    for (int i = 0; i < randomPointsHow; i = i + 1) //####[231]####
                    {//####[231]####
                        _lastElement_2 = i;//####[232]####
                        __omp_size_++;//####[233]####
                    }//####[234]####
                    _pi_2 = ParIteratorFactory.createParIterator(0, __omp_size_, 1, Pyjama.omp_get_num_threads(), ParIterator.Schedule.DYNAMIC, ParIterator.DEFAULT_CHUNKSIZE, false);//####[235]####
                    _omp_piVarContainer.add(_pi_2);//####[236]####
                    _pi_2.setThreadIdGenerator(new UniqueThreadIdGeneratorForOpenMP());//####[237]####
                    _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance.found = new int[Pyjama.omp_get_num_threads()];//####[238]####
                    for (int __omp__i__loop = 0; __omp__i__loop < Pyjama.omp_get_num_threads(); __omp__i__loop++) //####[239]####
                    {//####[239]####
                        _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance.found[__omp__i__loop] = 0;//####[240]####
                    }//####[241]####
                    _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance.notFound = new int[Pyjama.omp_get_num_threads()];//####[242]####
                    for (int __omp__i__loop = 0; __omp__i__loop < Pyjama.omp_get_num_threads(); __omp__i__loop++) //####[243]####
                    {//####[243]####
                        _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance.notFound[__omp__i__loop] = 0;//####[244]####
                    }//####[245]####
                    _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance.rys = rys;//####[246]####
                    _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance.rzs = rzs;//####[247]####
                    _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance.randomPoints = randomPoints;//####[248]####
                    _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance.randomPointsHow = randomPointsHow;//####[249]####
                    _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance.rxs = rxs;//####[250]####
                    _waitBarrier_2.countDown();//####[251]####
                } else {//####[252]####
                    try {//####[253]####
                        _waitBarrier_2.await();//####[253]####
                    } catch (InterruptedException __omp__ie) {//####[253]####
                        __omp__ie.printStackTrace();//####[253]####
                    }//####[253]####
                }//####[254]####
                _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2(_ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance);//####[255]####
                if (_imFinishedCounter_2.incrementAndGet() == PJPackageOnly.getThreadCountCurrentParallelRegion()) //####[256]####
                {//####[256]####
                    _waitBarrierAfter_2.countDown();//####[257]####
                } else {//####[258]####
                    try {//####[259]####
                        _waitBarrierAfter_2.await();//####[260]####
                    } catch (InterruptedException __omp__ie) {//####[261]####
                        __omp__ie.printStackTrace();//####[262]####
                    }//####[263]####
                }//####[264]####
                found = _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance.reducible_found.reduce(Reduction.IntegerSUM);//####[266]####
                notFound = _ompWorkSharedUserCode_SpheresByPointsRenderPyJava2_variables_instance.reducible_notFound.reduce(Reduction.IntegerSUM);//####[267]####
            } else {//####[268]####
                for (int i = 0; i < randomPointsHow; i = i + 1) //####[270]####
                {//####[271]####
                    try {//####[272]####
                        Point randomPoint = __omp_vars.randomPoints[i];//####[273]####
                        __omp_vars.rxs[i] = randomPoint.getX();//####[274]####
                        __omp_vars.rys[i] = randomPoint.getY();//####[275]####
                        __omp_vars.rzs[i] = randomPoint.getZ();//####[276]####
                        found++;//####[277]####
                    } catch (NullPointerException e) {//####[278]####
                        System.out.println("e:" + i);//####[279]####
                        ;//####[280]####
                        notFound++;//####[281]####
                    }//####[282]####
                }//####[283]####
            }//####[284]####
        }//####[286]####
        __omp_vars.rys = rys;//####[288]####
        __omp_vars.rzs = rzs;//####[289]####
        __omp_vars.randomPoints = randomPoints;//####[290]####
        __omp_vars.randomPointsHow = randomPointsHow;//####[291]####
        __omp_vars.notFound = notFound;//####[292]####
        __omp_vars.rxs = rxs;//####[293]####
        __omp_vars.found = found;//####[294]####
    }//####[295]####
//####[295]####
//####[295]####
    AtomicBoolean _omp_gui_once_only4 = new AtomicBoolean(false);//####[295]####
//####[297]####
    private void _ompNotifyMethod_0(TaskID id) {//####[297]####
        {//####[297]####
            _omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0 __omp_vars = (_omp__parallelRegionVarHolderClass_SpheresByPointsRenderPyJava0) id.getTaskArguments()[0];//####[298]####
            double[] rys = __omp_vars.rys;//####[299]####
            double[] rzs = __omp_vars.rzs;//####[300]####
            Point[] randomPoints = __omp_vars.randomPoints;//####[301]####
            int randomPointsHow = __omp_vars.randomPointsHow;//####[302]####
            int notFound = __omp_vars.notFound;//####[303]####
            double[] rxs = __omp_vars.rxs;//####[304]####
            int found = __omp_vars.found;//####[305]####
            PJPackageOnly.setThreadCountCurrentParallelRegion(1);//####[306]####
            _holderForPIFirst.set(true);//####[307]####
            System.out.println("found: " + found);//####[308]####
            System.out.println("notFound: " + notFound);//####[309]####
            {//####[310]####
                StdDraw3D.points(rxs, rys, rzs).setColor(Color.GREEN, 80);//####[311]####
            }//####[312]####
            boolean _omp_b_isInEventLoop = EventQueue.isDispatchThread();//####[313]####
            if (false == _omp_b_isInEventLoop) //####[315]####
            {//####[315]####
                final double[] _omp_gui_5rys = rys;//####[317]####
                final double[] _omp_gui_5rzs = rzs;//####[318]####
                final Point[] _omp_gui_5randomPoints = randomPoints;//####[319]####
                final int _omp_gui_5randomPointsHow = randomPointsHow;//####[320]####
                final int _omp_gui_5notFound = notFound;//####[321]####
                final double[] _omp_gui_5rxs = rxs;//####[322]####
                final int _omp_gui_5found = found;//####[323]####
                try {//####[325]####
                    SwingUtilities.invokeLater(new Runnable() {//####[325]####
//####[327]####
                        public void run() {//####[328]####
                            StdDraw3D.points(_omp_gui_5rxs, _omp_gui_5rys, _omp_gui_5rzs).setColor(Color.GREEN, 80);//####[329]####
                        }//####[330]####
                    });//####[330]####
                } catch (Exception e) {//####[332]####
                    e.printStackTrace();//####[333]####
                }//####[334]####
            } else {//####[335]####
                {//####[337]####
                    StdDraw3D.points(rxs, rys, rzs).setColor(Color.GREEN, 80);//####[338]####
                }//####[339]####
            }//####[340]####
        }//####[342]####
    }//####[343]####
//####[344]####
    AtomicBoolean _omp_gui_once_only5 = new AtomicBoolean(false);//####[344]####
}//####[344]####
