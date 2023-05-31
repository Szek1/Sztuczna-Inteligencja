import numpy as np
from matplotlib import pyplot as plt
from perceptron import SimplePerceptron
from sklearn.preprocessing import MinMaxScaler


def fake_data(m):  # m - liczba przypadków uczącyc
    # # losowanie liczb z danego przedziału
    # # X = np.random.uniform(low=[0,-1] + [-1]*(n-2),high=[2*np.pi,1]+[1]*(n-2),size=(m,n))
    X = np.random.uniform(low=[0, -1], high=[2 * np.pi, 1], size=(m, 2))
    y = []

    for i in range(m):
        if np.abs(np.sin(X[i, 0])) > np.abs([X[i, 1]]):
            y.append(-1)
        else:
            y.append(1)
    return X, y



def normalize(x):
    # x_min = np.min(x, axis=0)
    # x_max = np.max(x, axis=0)
    # X = 2 * (x - x_min)/ (x_max - x_min) - 1
    norm = MinMaxScaler(feature_range=(-1,1))
    X = norm.fit_transform(x)
    return X

def centers(X, m):
    return X[np.random.randint(0,X.shape[0],m)]


def upDim(X,c,sigma):
    z = np.ones((X.shape[0], c.shape[0]))
    for i in range(z.shape[0]):
        for j in range(z.shape[1]):
            z[i, j] = np.exp(-((X[i, 0] - c[j, 0]) ** 2 + (X[i, 1] - c[j, 1]) ** 2) / (2 * sigma ** 2))
    return z

if __name__ == '__main__':
    sigma = 0.33
    m = 80

    X, y = fake_data(1000)

    X = normalize(X)
    clf = SimplePerceptron(learning_rate=1.0)
    c = centers(X,m)
    clf.fit(upDim(X,c,sigma),y)

    # x_min, x_max = X[:, 0].min(), X[:, 0].max() + 0.1
    # y_min, y_max = X[:, 1].min(), X[:, 1].max() + 0.1
    x_min,x_max = -1,1.1
    y_min,y_max = -1,1.1
    xx, yy = np.meshgrid(np.arange(x_min, x_max,0.05), np.arange(y_min, y_max,0.05))
    Z = clf.predict(upDim(np.c_[xx.ravel(), yy.ravel()], c,sigma))
    Z = Z.reshape(xx.shape)
    plt.contourf(xx, yy, Z, levels=1, cmap='coolwarm')
    plt.contour(xx, yy, Z,levels = 0, colors=['black'])
    plt.scatter(X[:, 0], X[:, 1], c=y, cmap='coolwarm', s=5)
    plt.scatter(c[:, 0], c[:, 1], c='black')
    #
    clf = SimplePerceptron(learning_rate=1.0, sums=True)
    clf.fit(upDim(X, c,sigma), y)
    Z = clf.predict(upDim(np.c_[xx.ravel(), yy.ravel()], c,sigma))
    Z = Z.reshape(xx.shape)
    fig = plt.figure()
    ax = plt.subplot(projection='3d')
    surf = ax.plot_surface(xx, yy, Z, cmap='coolwarm')

    fig = plt.figure()
    ax = plt.subplot()
    ax.contourf(xx, yy, Z, levels=10, cmap='coolwarm')
    plt.contour(xx, yy, Z, levels=10, colors='black')
    plt.scatter(X[:,0],X[:,1],c=y,cmap='coolwarm', s=5)
    plt.scatter(c[:, 0], c[:,1], c='black')
    plt.show()

    # clf = SimplePerceptron(learning_rate=1.0)
    # clf.fit(X, y)
    # plt.scatter(X[:, 0], X[:, 1], c=y, cmap="coolwarm", s=5)
    # plt.xlabel('x1')
    # plt.ylabel('x2')
    # plt.show()


# def fake_data(m):  # m - liczba przypkladow uczacych
#     m_half = int(m / 2)
#     X_1 = np.random.rand(m, 1)
#     X_2_down = np.random.rand(m_half, 1) * 0.4
#     X_2_up = np.random.rand(m_half, 1) * 0.4 + 0.6
#     y_2_down = np.ones(m_half, dtype=np.int8)
#     y_2_up = -np.ones(m_half, dtype=np.int8)
#     X = np.c_[X_1, np.r_[X_2_up, X_2_down]]
#     y = np.r_[y_2_up, y_2_down]
#     return X, y

# clf.fit(X,y)
# print(f"w: {clf.w_}, k: {clf.k_}")
# print(f"ACC: {clf.score(X,y)}")
# x_1 = np.array([0.0, 1.0])
# x_2 = -(clf.w_[0] + clf.w_[1] * x_1) / clf.w_[2]
# plt.scatter(X[:, 0], X[:, 1], c=y, cmap="coolwarm", s=5)
# plt.plot(x_1, x_2, c="black")
# plt.show() w domu zadanie z wiki
