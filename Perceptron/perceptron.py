import numpy as np
from sklearn.base import BaseEstimator, ClassifierMixin


class SimplePerceptron(BaseEstimator, ClassifierMixin):

    def __init__(self, learning_rate=1.0, sums = False):
        self.learning_rate_ = learning_rate
        self.w_ = None  # wektor wag
        self.k_ = None  # licznik krokow
        self.class_labels_ = None  # wykaz etykiet klas
        self.sums = sums

    def fit(self, X, y):
        self.class_labels_ = np.unique(
            y)  # zakladamy, ze istnieja dokladnie 2 klasy; self.class_labels_[0] -> -1 self.class_labels_[1] ->
        m, n = X.shape
        yy = np.ones(m, dtype=np.int8)
        yy[y == self.class_labels_[0]] = -1
        self.k_ = 0
        self.w_ = np.zeros(n + 1)
        X_extended = np.c_[np.ones(m), X]

        while True:
            # X_extended = X_extended[np.random.permutation(m)] #ne trzeba
            E = []  # list indeksow blednie sklasyfikowanych punktow
            for i in range(m):
                s = self.w_.dot(X_extended[i])
                f = 1 if s > 0.0 else -1
                if f != yy[i]:
                    E.append(i)
                    break #nie trzeba
            if len(E) == 0 or self.k_ == 3000:
                return
            # E = [26, 37, 192]
            i = np.random.choice(E)  # i = E[np.random.randint(0,len(E))]
            self.w_ = self.w_ + self.learning_rate_ * yy[i] * X_extended[i]
            self.k_ +=1

    def predict(self, X):
        sums = self.decision_function(X)
        m = X.shape[0]
        # predictions = np.empty(m,dtype=self.class_labels_.dtype)
        predictions = np.zeros(m, dtype=np.int8) #check
        predictions[sums <= 0.0] = self.class_labels_[0]
        predictions[sums > 0.0] = self.class_labels_[1]
        if self.sums:
            return sums
        else:
            return predictions
    def decision_function(self, X):
        m = X.shape[0]
        return self.w_.dot(np.c_[np.ones(m), X].T)