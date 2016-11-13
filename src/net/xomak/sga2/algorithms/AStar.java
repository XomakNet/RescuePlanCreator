package net.xomak.sga2.algorithms;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Created by regis on 13.11.2016.
 */
public abstract class AStar<StepType extends AStarStep> {

    protected PriorityQueue<StepType> pendingSteps;
    protected StepType minPathLastStep;

    public AStar(final StepType startStep, final Comparator<StepType> comparator) {
        pendingSteps = new PriorityQueue<>(comparator);
        pendingSteps.add(startStep);
    }

    public AStar(final StepType startStep) {
        pendingSteps = new PriorityQueue<>();
        pendingSteps.add(startStep);
    }

    public StepType getMinPathLastStep() {
        return minPathLastStep;
    }

    public void run() {
        while (!pendingSteps.isEmpty()) {
            StepType currentStep = pendingSteps.poll();
            if (handleStep(currentStep)) {
                break;
            }
        }
    }

    protected abstract Set<StepType> getStepsFrom(final StepType currentStep);

    protected abstract boolean isLastStep(StepType step);

    protected boolean isShortestFound() {
        StepType minimalPendingStep = pendingSteps.peek();
        if (minPathLastStep != null && minPathLastStep.compareTo(minimalPendingStep) < 0) {
            return true;
        }
        return false;
    }

    protected abstract boolean shouldConsider(StepType step);

    protected boolean handleStep(StepType step) {

        if (isLastStep(step)) {
            if (minPathLastStep == null || step.compareTo(minPathLastStep) < 0) {
                minPathLastStep = step;
                System.out.println("Shortest found " + minPathLastStep);
                return isShortestFound();
            }
        } else {
            Set<StepType> stepsFromHere = getStepsFrom(step);
            for (StepType newStep : stepsFromHere) {
                if (shouldConsider(newStep)) {
                    pendingSteps.add(newStep);
                }
            }
        }
        return false;
    }
}

