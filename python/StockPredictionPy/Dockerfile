# 1. SETUP IMAGE
FROM python:3.13-slim

WORKDIR /app

RUN apt-get update && apt-get install -y \
    build-essential \
    libpq-dev \
    libcurl4-openssl-dev \
    libssl-dev \
    && rm -rf /var/lib/apt/lists/*

# Install Poetry
RUN pip install poetry

# 2. copy deps
COPY pyproject.toml poetry.lock ./

RUN poetry config virtualenvs.create false

# Install deps
RUN poetry install --no-root --only main

# 3. code copy
COPY src ./src

CMD ["python", "src/stockpredictionpy/main.py"]