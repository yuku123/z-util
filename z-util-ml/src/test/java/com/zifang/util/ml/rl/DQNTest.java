package com.zifang.util.ml.rl;

import com.zifang.util.numpy.DType;
import com.zifang.util.numpy.NdArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DQNTest {

    @Test
    public void testDQNInit() {
        DQN dqn = new DQN(4, 3, 16, 0.001, 0.99, 1.0, 0.995, 0.01, 100, 32, 10);
        
        assertNotNull(dqn.getOnlineNetwork());
        assertNotNull(dqn.getTargetNetwork());
        assertEquals(1.0, dqn.getEpsilon(), 0.01);
    }

    @Test
    public void testDQNSelectAction() {
        DQN dqn = new DQN(4, 3, 16, 0.001, 0.99, 1.0, 0.995, 0.01, 100, 32, 10);
        
        // Create a simple state
        NdArray state = NdArray.array(new double[]{1.0, 2.0, 3.0, 4.0}, DType.FLOAT32).reshape(1, 4);
        
        // With epsilon=1.0, should select random action
        int action = dqn.selectAction(state);
        assertTrue(action >= 0 && action < 3, "Action should be between 0 and 2");
    }

    @Test
    public void testDQNTrainingStep() {
        DQN dqn = new DQN(4, 3, 16, 0.001, 0.99, 1.0, 0.995, 0.01, 100, 32, 10);
        
        NdArray state = NdArray.array(new double[]{1.0, 2.0, 3.0, 4.0}, DType.FLOAT32).reshape(1, 4);
        NdArray nextState = NdArray.array(new double[]{2.0, 3.0, 4.0, 5.0}, DType.FLOAT32).reshape(1, 4);
        
        // Train should not throw
        dqn.train(state, 1, 1.0, nextState, false);
        
        // Epsilon should have decayed slightly
        assertTrue(dqn.getEpsilon() < 1.0);
    }
}
