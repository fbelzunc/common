package com.cisco.vss.foundation.flowcontext;


import org.slf4j.MDC;

/**
 * A tool manage thread local of FlowContext.
 * It creates FlowContext, restores FlowContext from a string,
 * and converts FlowContext to string.
 *
 * @author Yehudit Glass
 */
public final class FlowContextFactory {

    private static final ThreadLocal<FlowContext> FLOW_CONTEXT_THREAD_LOCAL = new ThreadLocal<FlowContext>();

    private FlowContextFactory() {
    }

    /**
     * Create FlowContext and put it on ThreadLocal.
     * If there is a FlowContext in the ThreadLocal it will be overridden.
     *
     * @return The flowContext that is now in the ThreadLocal
     */

    public static synchronized FlowContext createFlowContext() {
        final FlowContext flowContext = new FlowContextImpl();
        addFlowContext(flowContext);
        return flowContext;
    }

    /**
     * Create FlowContext and put it on ThreadLocal.
     * If there is a FlowContext in the ThreadLocal it will be overridden.
     *
     * @param uuid
     * @return The flowContext that is now in the ThreadLocal
     */
    public static FlowContext createFlowContext(final String uuid) {
        if (null == uuid) {
            throw new IllegalArgumentException("Flow context cannot be null");
        }
        final FlowContext flowContext = new FlowContextImpl(uuid);
        addFlowContext(flowContext);
        return flowContext;
    }

    public static void clearFlowcontext() {
        FLOW_CONTEXT_THREAD_LOCAL.set(null);
        MDC.remove("flowCtxt");
    }

    /**
     * De-serialize a Sting to FlowContext object and add it to ThreadLocal.
     * If there is a FlowContext in the ThreadLocal it will be overridden.
     *
     * @param flowContextString - a String represents FlowContext object.
     * @return flowContext or null if flowContextString is not valid.
     */
    public static FlowContext deserializeNativeFlowContext(final String flowContextString) {

        if (flowContextString == null) {
            return null;
        }

        final FlowContext flowContext =
                FlowContextImpl.deserializeNativeFlowContext(flowContextString);
        addFlowContext(flowContext);

        return flowContext;
    }

    /**
     * Add flowContext to ThreadLocal when coming to another component.
     *
     * @param flowContext
     */
    public static void addFlowContext(final FlowContext flowContext) {

        if (null == flowContext) {
            clearFlowcontext();
            return;
        }

        FLOW_CONTEXT_THREAD_LOCAL.set(flowContext);
        MDC.put("flowCtxt", flowContext.toString());
    }

    /**
     * Serialize flowContext into a String.
     *
     * @return String that represent the value of the flowContext in the ThreadLocal.
     *         if the ThreadscLocal is empty, return empty String "".
     */
    public static String serializeNativeFlowContext() {
        final FlowContext flowCntext = FLOW_CONTEXT_THREAD_LOCAL.get();
        if (flowCntext != null) {
            return ((FlowContextImpl) flowCntext).serializeNativeFlowContext();
        }
        return "";
    }

    /**
     * @return the value of the flowContext in the ThreadLocal,
     *         if the the ThreadLocal is empty, return null.
     */
    public static FlowContext getFlowContext() {
        return FLOW_CONTEXT_THREAD_LOCAL.get();
    }

    public static void incrementInnerTxCounter() {
        FlowContext flowContext = FLOW_CONTEXT_THREAD_LOCAL.get();
        if (flowContext != null) ((FlowContextImpl) flowContext).incrementInnerTxCounter();
    }

    public static void resetInnerTxCounter() {
        FlowContext flowContext = FLOW_CONTEXT_THREAD_LOCAL.get();
        if (flowContext != null) ((FlowContextImpl) flowContext).resetInnerTxCounter();
    }
}
