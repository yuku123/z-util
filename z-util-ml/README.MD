# z-util-ml

纯 Java 实现的机器学习算法工具箱，覆盖传统算法 + 深度学习 + 强化学习 + 进化算法。无外部 ML 框架依赖，可直接在 JVM 上训练与推理。

> 包路径：`com.zifang.util.ml.*`

## Features

### 神经网络 (`nn` / `nnet`)

- **层 (Layer)**：`Dense`、`Conv2d`、`MaxPool2d`、`AvgPool2d`、`RNN`、`LSTM`、`Dropout`、`Flatten`、`Reshape`、`Softmax`、`BatchNorm2d`、`LayerNorm`
- **激活函数**：`ReLU`、`LeakyReLU`、`ELU`、`GELU`、`Swish`、`Tanh`、`Softplus`
- **高级模型**：`GAN`、`VAE`、`TransformerEncoder`
- **组合容器**：`Sequential`、`Module`（PyTorch 风格）
- **传统 MLP**：`NeuralNetwork` / `NeuralNet` + `InputLayer` / `HiddenLayer` / `OutputLayer` + `ActivationFunction`

### 线性模型 (`linear`)

- `LinearRegression` — 线性回归
- `LogisticRegression` — 逻辑回归
- `Perceptron` — 感知机
- `SVM` — 支持向量机

### 树模型 (`tree`)

- `DecisionTree` — 决策树（CART）
- `RandomForest` — 随机森林
- `XGBoost` — 梯度提升树
- `AdaBoost` — AdaBoost 集成

### 无监督 / 降维

- **聚类 (`clustering`)**：`KMeans`、`DBSCAN`、`GMM`（高斯混合）、`HierarchicalClustering`（层次聚类）
- **降维 (`decomposition`)**：`PCA`、`tSNE`、`UMAP`
- **关联规则 (`association`)**：`Apriori`、`FPGrowth`
- **异常检测 (`anomaly`)**：`LOF`、`IsolationForest`、`OneClassSVM`

### 序列模型 (`sequence`)

- `HMM` — 隐马尔可夫模型
- `CRF` — 条件随机场

### 强化学习 (`rl`)

- `QLearning` — Q-Learning
- `SARSA` — SARSA
- `DQN` — 深度 Q 网络
- `PolicyGradient` — 策略梯度

### 遗传算法 (`ga`)

- `GeneticAlgorithmEngine` — GA 引擎
- `Individual`、`Population`、`BinaryGenotype`
- `SelectionOperator`、`CrossoverOperator`（`SinglePointCrossover`）、`MutationOperator`（`BinaryMutation`）
- `FitnessFunction`、`TerminationCondition`（`TargetFitnessTermination`）

### 优化器 (`optim`)

- `SGD`、`Momentum`、`Nesterov`
- `Adam`、`RMSprop`、`Adagrad`
- 学习率调度：`StepLR`、`CosineAnnealingLR`、`ReduceLROnPlateau`、`LrScheduler`

### 损失函数 (`loss`)

- `MSELoss`、`L1Loss`
- `CrossEntropyLoss`、`BinaryCrossEntropyLoss`、`BCEWithLogitsLoss`

### 集成学习 (`ensemble`)

- `Bagging`、`Voting`、`Stacking`、`Estimator`

### 数据 (`data`)

- `Dataset`、`TensorDataset`、`DataLoader`

## Quick Start

### 1. 训练一个 MLP（神经网络）

```java
Sequential model = new Sequential(
    new Dense(784, 256),
    new ReLU(),
    new Dropout(0.2),
    new Dense(256, 10),
    new Softmax()
);

Optimizer opt = new Adam(model.parameters(), 0.001);
LossFunction lossFn = new CrossEntropyLoss();

// 训练循环
for (int epoch = 0; epoch < 10; epoch++) {
    for (Batch batch : trainLoader) {
        var out = model.forward(batch.x);
        var loss = lossFn.forward(out, batch.y);
        model.zeroGrad();
        loss.backward();
        opt.step();
    }
}
```

### 2. K-Means 聚类

```java
double[][] data = ...; // (N, 2) 样本
KMeans kmeans = new KMeans(3, 100);   // 3 个簇，最多 100 轮
int[] labels = kmeans.fitPredict(data);
```

### 3. 线性回归

```java
double[] x = {1, 2, 3, 4, 5};
double[] y = {2.1, 4.0, 5.9, 8.1, 9.9};
LinearRegression lr = new LinearRegression();
lr.fit(x, y);
double pred = lr.predict(6);   // ≈ 12.0
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-ml</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```