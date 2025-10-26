import numpy as np
import pandas as pd


def generate_data(n=10):
    """
    Generate a sample dataset using NumPy.
    Returns a pandas DataFrame.
    """
    np.random.seed(42)  # for reproducibility
    data = {
        "id": np.arange(1, n + 1),
        "age": np.random.randint(18, 70, size=n),
        "income": np.random.normal(50000, 15000, size=n).astype(int),
        "purchases": np.random.poisson(lam=3, size=n),
    }
    return pd.DataFrame(data)


def summarize_data(df):
    """
    Compute summary statistics with pandas.
    """
    summary = df.describe(include="all")
    print("📊 Summary statistics:")
    print(summary)
    print()

    print("💡 Average income per age group:")
    print(
        df.groupby(pd.cut(df["age"], bins=[18, 30, 40, 50, 60, 70]), observed=True)[
            "income"
        ].mean()
    )


def main():
    df = generate_data(20)
    print("✅ Sample dataset:")
    print(df.head(), "\n")

    summarize_data(df)

    # Add a derived column
    df["income_per_purchase"] = np.where(
        df["purchases"] > 0, df["income"] / df["purchases"], np.nan
    )

    print("\n💰 Added column 'income_per_purchase':")
    print(df.head())


if __name__ == "__main__":
    main()
