# Quick Start: Repository Migration

## For the Repository Owner (rostyantonov)

You need to execute the migration from a machine that has access to both:
- EPAM GitLab (git.epam.com) with your credentials
- GitHub (github.com)

### Simple 3-Step Process

#### Step 1: Clone this repository
```bash
git clone https://github.com/rostyantonov/advent-of-code
cd advent-of-code
```

#### Step 2: Run the migration script
```bash
./migrate-repository.sh
```

The script will ask you to choose an authentication method:
- Option 1: HTTPS with credentials (username/password)
- Option 2: HTTPS with personal access token (recommended)
- Option 3: SSH (if you have SSH keys configured)

#### Step 3: Verify the migration
After the script completes, verify that all branches are present:
```bash
git branch -r
```

### Alternative: One-Command Mirror

If you prefer a single command without the interactive script:

```bash
# Replace <TOKEN> with your EPAM GitLab personal access token
git clone --mirror https://oauth2:<TOKEN>@git.epam.com/rostyslav_antonov/adventofcode temp-mirror
cd temp-mirror
git push --mirror https://github.com/rostyantonov/advent-of-code
cd ..
rm -rf temp-mirror
```

### Getting a Personal Access Token

1. Log in to EPAM GitLab: https://git.epam.com
2. Go to User Settings → Access Tokens
3. Create a new token with `read_repository` scope
4. Copy the token and use it in the migration command

### Need Help?

See the detailed documentation:
- [MIGRATION_GUIDE.md](MIGRATION_GUIDE.md) - Full guide with troubleshooting
- [MIGRATION_STATUS.md](MIGRATION_STATUS.md) - Current status and context

### What This Will Do

✅ Copy all branches from EPAM GitLab to GitHub  
✅ Copy all tags  
✅ Preserve complete commit history  
✅ Keep all Advent of Code solutions intact  

The EPAM repository will remain unchanged (read-only operation).
