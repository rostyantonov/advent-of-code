#!/bin/bash
# Setup script to fetch all branches and checkout experiment branch

# Configure git to fetch all branches
git config remote.origin.fetch "+refs/heads/*:refs/remotes/origin/*"

# Fetch all branches from remote
git fetch --all

# Checkout the experiment branch as the starting branch
git checkout experiment

echo "Repository setup complete. All branches fetched and 'experiment' branch is now active."
