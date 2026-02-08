#!/bin/bash
set -e
# Setup script to fetch all branches and checkout experiment branch

echo "Configuring git to fetch all branches..."
git config remote.origin.fetch "+refs/heads/*:refs/remotes/origin/*"

echo "Fetching all branches from remote..."
git fetch --all

echo "Checking out the experiment branch..."
if git rev-parse --verify origin/experiment > /dev/null 2>&1; then
    git checkout experiment
else
    echo "Error: 'experiment' branch not found on remote."
    exit 1
fi

echo "Repository setup complete. All branches fetched and 'experiment' branch is now active."
