#!/bin/bash

# Repository Migration Script
# Migrates all branches and tags from EPAM GitLab to GitHub

set -e  # Exit on any error

# Configuration
SOURCE_REPO="https://git.epam.com/rostyslav_antonov/adventofcode"
TARGET_REPO="https://github.com/rostyantonov/advent-of-code"
TEMP_DIR="advent-of-code-migration-temp"

echo "=========================================="
echo "Repository Migration Script"
echo "=========================================="
echo "Source: $SOURCE_REPO"
echo "Target: $TARGET_REPO"
echo "=========================================="
echo ""

# Check if git is installed
if ! command -v git &> /dev/null; then
    echo "Error: git is not installed"
    exit 1
fi

# Prompt for authentication method
echo "Select authentication method:"
echo "1) HTTPS with credentials"
echo "2) HTTPS with personal access token"
echo "3) SSH"
read -p "Enter choice (1-3): " auth_choice

case $auth_choice in
    1)
        read -p "Enter your EPAM GitLab username: " username
        read -s -p "Enter your password: " password
        echo ""
        SOURCE_REPO="https://${username}:${password}@git.epam.com/rostyslav_antonov/adventofcode"
        ;;
    2)
        read -s -p "Enter your personal access token: " token
        echo ""
        SOURCE_REPO="https://oauth2:${token}@git.epam.com/rostyslav_antonov/adventofcode"
        ;;
    3)
        SOURCE_REPO="git@git.epam.com:rostyslav_antonov/adventofcode.git"
        ;;
    *)
        echo "Invalid choice"
        exit 1
        ;;
esac

# Clean up any existing temporary directory
if [ -d "$TEMP_DIR" ]; then
    echo "Removing existing temporary directory..."
    rm -rf "$TEMP_DIR"
fi

# Step 1: Clone the source repository as a mirror
echo ""
echo "Step 1: Cloning source repository (mirror mode)..."
git clone --mirror "$SOURCE_REPO" "$TEMP_DIR"

# Navigate to the temporary directory
cd "$TEMP_DIR"

# Step 2: Add GitHub as a remote
echo ""
echo "Step 2: Adding GitHub as remote..."
git remote add github "$TARGET_REPO"

# Step 3: Push everything to GitHub
echo ""
echo "Step 3: Pushing all branches and tags to GitHub..."
echo "This may take a while depending on repository size..."
git push --mirror github

# Step 4: Verification
echo ""
echo "Step 4: Verifying migration..."
cd ..

# Clone from GitHub to verify
VERIFY_DIR="advent-of-code-verify"
if [ -d "$VERIFY_DIR" ]; then
    rm -rf "$VERIFY_DIR"
fi

git clone "$TARGET_REPO" "$VERIFY_DIR"
cd "$VERIFY_DIR"
git fetch --all

echo ""
echo "Branches in GitHub repository:"
git branch -r

echo ""
echo "Tags in GitHub repository:"
git tag

cd ..

# Cleanup
echo ""
read -p "Migration complete! Remove temporary directories? (y/n): " cleanup
if [ "$cleanup" = "y" ] || [ "$cleanup" = "Y" ]; then
    rm -rf "$TEMP_DIR"
    rm -rf "$VERIFY_DIR"
    echo "Temporary directories removed."
else
    echo "Temporary directories kept:"
    echo "  - $TEMP_DIR (mirror)"
    echo "  - $VERIFY_DIR (verification)"
fi

echo ""
echo "=========================================="
echo "Migration completed successfully!"
echo "=========================================="
echo "All branches and tags have been migrated to:"
echo "$TARGET_REPO"
echo "=========================================="
