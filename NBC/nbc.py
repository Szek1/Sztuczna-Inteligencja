from sklearn.base import BaseEstimator, ClassifierMixin
import numpy as np


class DiscreteNaiveBayes(BaseEstimator, ClassifierMixin):

    def __init__(self, domain_sizes, laplace=False, logarytm = False):  # successes/ trails <- (successes + 1) / (trails + domain_size)
        self.domain_sizes_ = domain_sizes
        self.laplace_ = laplace
        self.logaritmic_ = logarytm
        self.PY_ = None  # 1D vector with class probabilities (a priori)
        self.P_ = None  # 3D array with all conditional probabilities
        self.class_labels_ = None

    def fit(self, X, y):
        self.class_labels_ = np.unique(y)
        m, n = X.shape
        K = self.class_labels_.size
        yy = np.zeros(m, dtype=np.int16)
        for k, label in enumerate(self.class_labels_):
            yy[y == label] = k  # mapping labels to: 0, 1, 2...l

        self.PY_ = np.zeros(K)
        for k in range(K):
            self.PY_[k] = np.sum(yy == k)

        max_domain_size = np.max(self.domain_sizes_)
        self.P_ = np.zeros((K, n, max_domain_size), )
        for i in range(m):
            for j in range(n):
                self.P_[yy[i], j, X[i, j]] += 1

        for k in range(K):
            if self.laplace_:
                for j in range(n):
                    self.P_[k, j] = (self.P_[k, j] + 1) / (self.PY_[k] + self.domain_sizes_[j])
            else:
                self.P_[k] /= self.PY_[k]
            self.PY_[k] /= m

    def predict(self, X):
        return self.class_labels_[np.argmax(self.predict_proba(X), axis=1)]

    def predict_proba(self, X):
        # prod_{j = 1}^n P(X_j = x_j | Y = y) * P(Y=y)
        m, n = X.shape
        K = self.class_labels_.size
        scores = np.zeros((m, K))
        for i in range(m):
            for k in range(K):
                if self.logaritmic_:
                    scores[i, k] += np.log(self.PY_[k])
                    for j in range(n):
                        scores[i, k] += np.log(self.P_[k, j, X[i, j]])
                else:
                    scores[i, k] = self.PY_[k]
                    scores[i, k] *= np.prod(self.P_[k, np.arange(n), X[i]])
        return scores
