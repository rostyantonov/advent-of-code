# Repository Migration Guide

## Objective
Clone all content and branches from the EPAM GitLab repository to this GitHub repository.

## Source Repository
- **URL**: https://git.epam.com/rostyslav_antonov/adventofcode
- **Type**: Private EPAM GitLab instance (requires authentication)

## Target Repository
- **URL**: https://github.com/rostyantonov/advent-of-code
- **Type**: Public GitHub repository

## Prerequisites

1. **Authentication to EPAM GitLab**
   - You need valid credentials for git.epam.com
   - Either username/password or personal access token
   - Or SSH key configured for GitLab

2. **Git Configuration**
   - Git installed locally
   - Access to both source and target repositories

## Migration Steps

### Option 1: Mirror Clone (Recommended)

This approach creates a complete mirror including all branches and tags:

```bash
# 1. Create a bare clone of the source repository
git clone --mirror https://git.epam.com/rostyslav_antonov/adventofcode advent-of-code-mirror

# 2. Navigate into the mirrored repository
cd advent-of-code-mirror

# 3. Add the GitHub repository as a remote
git remote add github https://github.com/rostyantonov/advent-of-code

# 4. Push everything (all branches and tags) to GitHub
git push --mirror github

# 5. Clean up (optional)
cd ..
rm -rf advent-of-code-mirror
```

### Option 2: Regular Clone with All Branches

This approach gives you a working repository:

```bash
# 1. Clone the source repository
git clone https://git.epam.com/rostyslav_antonov/adventofcode advent-of-code-temp

# 2. Navigate into the repository
cd advent-of-code-temp

# 3. Fetch all branches
git fetch --all

# 4. List all remote branches
git branch -r

# 5. Create local branches for all remote branches
for branch in $(git branch -r | grep -v '\->'); do
    branch_name=${branch#origin/}
    git checkout -b "$branch_name" "$branch" 2>/dev/null || true
done

# 6. Add GitHub as a new remote
git remote add github https://github.com/rostyantonov/advent-of-code

# 7. Push all branches
git push github --all

# 8. Push all tags
git push github --tags

# 9. Clean up (optional)
cd ..
rm -rf advent-of-code-temp
```

### Option 3: Using the Provided Script

A migration script is provided in this repository for convenience:

```bash
chmod +x migrate-repository.sh
./migrate-repository.sh
```

## Authentication Methods

### Using Personal Access Token (Recommended for HTTPS)

1. Generate a personal access token in EPAM GitLab:
   - Go to GitLab Settings > Access Tokens
   - Create token with `read_repository` scope

2. Use the token in the URL:
   ```bash
   git clone https://oauth2:<TOKEN>@git.epam.com/rostyslav_antonov/adventofcode
   ```

### Using SSH

1. Add your SSH key to EPAM GitLab
2. Use SSH URL instead:
   ```bash
   git clone git@git.epam.com:rostyslav_antonov/adventofcode.git
   ```

## Post-Migration Verification

After migration, verify that all branches have been transferred:

```bash
# Check branches in GitHub repository
git clone https://github.com/rostyantonov/advent-of-code
cd advent-of-code
git fetch --all
git branch -r
```

## Troubleshooting

### Authentication Failed
- Verify your credentials are correct
- Check if your token has the required permissions
- Ensure you have access to the EPAM GitLab repository

### SSL Certificate Issues
If you encounter SSL certificate errors:
```bash
git config --global http.sslVerify false  # Use with caution
```

### Large Repository
For large repositories, increase buffer size:
```bash
git config --global http.postBuffer 524288000
```

## Notes

- The EPAM GitLab repository is not publicly accessible
- Migration requires valid authentication to git.epam.com
- All branches, tags, and commit history will be preserved
- The migration is a one-time operation; subsequent updates need separate sync
