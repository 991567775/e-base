package cn.ezeyc.core.core;

import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.annotation.After;
import org.noear.solon.annotation.Before;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Options;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.handle.*;
import org.noear.solon.core.util.ConsumerEx;
import org.noear.solon.core.util.PathUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 请求control注解重写
 * @author wz
 */
public class HandlerLoader extends HandlerAide {
    protected BeanWrap bw;
    protected Render bRender;
    protected Mapping bMapping;
    protected String bPath;
    protected boolean bRemoting;

    protected boolean allowMapping;

    public HandlerLoader(BeanWrap wrap) {
        bMapping = wrap.clz().getAnnotation(Mapping.class);

        if (bMapping == null) {
            initDo(wrap, null, wrap.remoting(), null, true);
        } else {
            String bPath = Utils.annoAlias(bMapping.value(), bMapping.path());
            initDo(wrap, bPath, wrap.remoting(), null, true);
        }
    }

    public HandlerLoader(BeanWrap wrap, String mapping) {
        initDo(wrap, mapping, wrap.remoting(), null, true);
    }

    public HandlerLoader(BeanWrap wrap, String mapping, boolean remoting) {
        initDo(wrap, mapping, remoting, null, true);
    }

    public HandlerLoader(BeanWrap wrap, String mapping, boolean remoting, MyControl render, boolean allowMapping) {
        initDo(wrap, mapping, remoting, render, allowMapping);
    }

    private void initDo(BeanWrap wrap, String mapping, boolean remoting, MyControl render, boolean allowMapping) {
        bw = wrap;
        bRender = render;
        this.allowMapping = allowMapping;

        if (mapping != null) {
            bPath = mapping;
        }

        bRemoting = remoting;
    }

    /**
     * mapping expr
     */
    public String mapping() {
        return bPath;
    }

    /**
     * 加载 Action 到目标容器
     *
     * @param slots 接收加载结果的容器（槽）
     */
    public void load(HandlerSlots slots) {
        load(bRemoting, slots);
    }

    /**
     * 加载 Action 到目标容器
     *
     * @param all   加载全部函数（一般 remoting 会全部加载）
     * @param slots 接收加载结果的容器（槽）
     */
    public void load(boolean all, HandlerSlots slots) {
        if (Handler.class.isAssignableFrom(bw.clz())) {
            loadHandlerDo(slots);
        } else {
            loadActionDo(slots, all || bRemoting);
        }
    }

    /**
     * 加载处理
     */
    protected void loadHandlerDo(HandlerSlots slots) {
        if (bMapping == null) {
            throw new IllegalStateException(bw.clz().getName() + " No @Mapping!");
        }

        Handler handler = bw.raw();
        Set<MethodType> v0 = MethodTypeUtil.findAndFill(new HashSet<>(), t -> bw.annotationGet(t) != null);
        if (v0.size() == 0) {
            v0 = new HashSet<>(Arrays.asList(bMapping.method()));
        }

        slots.add(bMapping, v0, handler);
    }


    /**
     * 加载 Action 处理
     */
    protected void loadActionDo(HandlerSlots slots, boolean all) {
        String mPath;
        if (bPath == null) {
            bPath = "";
        }
        Set<MethodType> bMethod = new HashSet<>();

        loadControllerAide(bMethod);

        Set<MethodType> mMethod;
        Mapping mMap;
        int mIndex = 0;

        //只支持 public 函数为 Action
        for (Method method : bw.clz().getDeclaredMethods()) {
            mMap = method.getAnnotation(Mapping.class);
            mIndex = 0;
            mMethod = new HashSet<>();

            //获取 action 的methodTypes
            MethodTypeUtil.findAndFill(mMethod, t -> method.getAnnotation(t) != null);

            //构建path and method
            if (mMap != null) {
                mPath = Utils.annoAlias(mMap.value(), mMap.path());

                if (mMethod.size() == 0) {
                    //如果没有找到，则用Mapping上自带的
                    mMethod.addAll(Arrays.asList(mMap.method()));
                }
                mIndex = mMap.index();
            } else {
                mPath = method.getName();

                if (mMethod.size() == 0) {
                    //获取 controller 的methodTypes
                    MethodTypeUtil.findAndFill(mMethod, t -> bw.clz().getAnnotation(t) != null);
                }

                if (mMethod.size() == 0) {
                    //如果没有找到，则用Mapping上自带的；或默认POST
                    if (bMapping == null) {
                        mMethod.add(MethodType.POST);
                    } else {
                        mMethod.addAll(Arrays.asList(bMapping.method()));
                    }
                }
            }

            //如果是service，method 就不需要map
            if (mMap != null || all) {
                String newPath = PathUtil.mergePath(bPath, mPath);

                Action action = createAction(bw, method, mMap, newPath, bRemoting);

                //m_method 必须之前已准备好，不再动  //用于支持 Cors
                loadActionAide(method, action, mMethod);
                if (bMethod.size() > 0 &&
                        mMethod.contains(MethodType.HTTP) == false &&
                        mMethod.contains(MethodType.ALL) == false) {
                    //用于支持 Cors
                    mMethod.addAll(bMethod);
                }

                for (MethodType m1 : mMethod) {
                    if (mMap == null) {
                        slots.add(newPath, m1, action);
                    } else {
                        if ((mMap.after() || mMap.before())) {
                            if (mMap.after()) {
                                slots.after(newPath, m1, mIndex, action);
                            } else {
                                slots.before(newPath, m1, mIndex, action);
                            }
                        } else {
                            slots.add(newPath, m1, action);
                        }
                    }
                }
            }
        }
    }


    protected void loadControllerAide(Set<MethodType> methodSet) {
        for (Annotation anno : bw.clz().getAnnotations()) {
            if (anno instanceof Before) {
                addDo(((Before) anno).value(), (b) -> this.before(Solon.context().getBeanOrNew(b)));
            } else if (anno instanceof After) {
                addDo(((After) anno).value(), (f) -> this.after(Solon.context().getBeanOrNew(f)));
            } else {
                for (Annotation anno2 : anno.annotationType().getAnnotations()) {
                    if (anno2 instanceof Before) {
                        addDo(((Before) anno2).value(), (b) -> this.before(Solon.context().getBeanOrNew(b)));
                    } else if (anno2 instanceof After) {
                        addDo(((After) anno2).value(), (f) -> this.after(Solon.context().getBeanOrNew(f)));
                    } else if (anno2 instanceof Options) {
                        //用于支持 Cors
                        methodSet.add(MethodType.OPTIONS);
                    }
                }
            }
        }
    }

    protected void loadActionAide(Method method, Action action, Set<MethodType> methodSet) {
        for (Annotation anno : method.getAnnotations()) {
            if (anno instanceof Before) {
                addDo(((Before) anno).value(), (b) -> action.before(Solon.context().getBeanOrNew(b)));
            } else if (anno instanceof After) {
                addDo(((After) anno).value(), (f) -> action.after(Solon.context().getBeanOrNew(f)));
            } else {
                for (Annotation anno2 : anno.annotationType().getAnnotations()) {
                    if (anno2 instanceof Before) {
                        addDo(((Before) anno2).value(), (b) -> action.before(Solon.context().getBeanOrNew(b)));
                    } else if (anno2 instanceof After) {
                        addDo(((After) anno2).value(), (f) -> action.after(Solon.context().getBeanOrNew(f)));
                    } else if (anno2 instanceof Options) {
                        //用于支持 Cors
                        if (methodSet.contains(MethodType.HTTP) == false &&
                                methodSet.contains(MethodType.ALL) == false) {
                            methodSet.add(MethodType.OPTIONS);
                        }
                    }
                }
            }
        }
    }

    /**
     * 构建 Action
     */
    protected Action createAction(BeanWrap bw, Method method, Mapping mp, String path, boolean remoting) {
        if (allowMapping) {
            return new Action(bw, this, method, mp, path, remoting, bRender);
        } else {
            return new Action(bw, this, method, null, path, remoting, bRender);
        }
    }

    /**
     * 附加触发器（前后置处理）
     */
    private static <T> void addDo(T[] ary, ConsumerEx<T> fun) {
        if (ary != null) {
            for (T t : ary) {
                try {
                    fun.accept(t);
                } catch (RuntimeException ex) {
                    throw ex;
                } catch (Throwable ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
