import numpy as np
from nbc import DiscreteNaiveBayes
import time
from sklearn.naive_bayes import CategoricalNB, GaussianNB
from sklearn.neighbors import KNeighborsClassifier
from sklearn.neural_network import MLPClassifier


def read_wine_data(filepath):
    D = np.genfromtxt(filepath, delimiter=",")
    y = D[:, 0].astype(np.int8)
    X = D[:, 1:]
    return X, y


def train_test_split(X, y, train_ratio=0.75, seed=1):
    np.random.seed(seed)
    m = X.shape[0]
    indexes = np.random.permutation(m)
    X = X[indexes]
    y = y[indexes]
    index = int(np.round(train_ratio * m))
    X_train = X[:index]
    y_train = y[:index]
    X_test = X[index:]
    y_test = y[index:]
    return X_train, y_train, X_test, y_test


def discretize(X, bins=5, mins_ref=None, maxes_ref=None):
    if mins_ref is None:
        mins_ref = np.min(X, axis=0)
        maxes_ref = np.max(X, axis=0)
    X_d = np.clip(((X - mins_ref) / (maxes_ref - mins_ref) * bins).astype(np.int8), 0, bins - 1)
    return X_d, mins_ref, maxes_ref


if __name__ == '__main__':
    # data_folder = "I:/S5/SI/Lab3/data/"
    data_folder = "C:/Users/Michal/Desktop/S5/SI/NBC/data/"
    bins = 5
    # X, y = read_wine_data(data_folder + "wine.data")
    X, y = read_wine_data(data_folder + "spambase.data")
    X = np.tile(X, 10)
    n = X.shape[1]
    X_train, y_train, X_test, y_test = train_test_split(X, y, train_ratio=0.75, seed=0)
    X_d_train, mins_ref, maxes_ref = discretize(X_train, bins=bins)
    X_d_test, _, _ = discretize(X_test, bins=bins, mins_ref=mins_ref, maxes_ref=maxes_ref)
    domain_sizes = bins * np.ones(n, dtype=np.int8)
    # clf = DiscreteNaiveBayes(domain_sizes, laplace=True)
    # clf.fit(X_d_train, y_train)
    # t1 = time.time()
    # scores = clf.predict_proba(X_d_test)
    # t2 = time.time()
    # print(f"TIME: {t2 - t1} s.")
    # predictions = clf.predict(X_d_test)

    clfs = [DiscreteNaiveBayes(domain_sizes, laplace=False, logarytm=False),
            CategoricalNB(min_categories=domain_sizes),
            GaussianNB(),
            KNeighborsClassifier(n_neighbors=3),
            MLPClassifier(alpha=1e-05, hidden_layer_sizes=(16, 8), random_state=1, solver='lbfgs', max_iter=10000)
            ]
    for clf in clfs:
        print("---")
        print(f"{clf.__class__.__name__}")
        clf.fit(X_d_train, y_train)
        print(f"ACC TEST: {clf.score(X_d_test, y_test)}")
        print(f"ACC TRAIN: {clf.score(X_d_train, y_train)}")
        if isinstance(clf, GaussianNB):
            print(f"{clf.__class__.__name__} (NON_DISCRETE DATA)")
            clf.fit(X_d_train, y_train)
            print(f"ACC TEST (NON_DISCRETE): {clf.score(X_d_test, y_test)}")

    # print(clf.P_)

    # print(X_d_train)
    # print(X_d_test)

    # print(y_test)
    # print(predictions)

    # print(scores)
    # print(clf.PY_)
    # print(clf.P_)
