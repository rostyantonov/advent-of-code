# Migration with Access Token

## Access Token Provided
An access token has been provided: `HuzzPDnVQ68xsmx1Gmj4`

## Issue: Network Access Required

The EPAM GitLab instance (git.epam.com) is not accessible from this automated environment because:
- ❌ DNS cannot resolve `git.epam.com` (corporate internal domain)
- ❌ The domain is only accessible from EPAM's corporate network
- ❌ Requires VPN or internal network access

## Solution: Execute Locally

You need to run the migration from a machine that has access to EPAM's network.

### Option 1: One-Line Migration (Fastest)

Run this single command from a machine with EPAM network access:

```bash
cd /tmp && \
git clone --mirror https://oauth2:HuzzPDnVQ68xsmx1Gmj4@git.epam.com/rostyslav_antonov/adventofcode advent-mirror && \
cd advent-mirror && \
git push --mirror https://github.com/rostyantonov/advent-of-code && \
cd .. && \
rm -rf advent-mirror && \
echo "Migration complete!"
```

### Option 2: Step-by-Step Migration

```bash
# Step 1: Clone the EPAM repository (mirror mode includes all branches)
git clone --mirror https://oauth2:HuzzPDnVQ68xsmx1Gmj4@git.epam.com/rostyslav_antonov/adventofcode advent-mirror

# Step 2: Navigate to the mirror
cd advent-mirror

# Step 3: Push everything to GitHub
git push --mirror https://github.com/rostyantonov/advent-of-code

# Step 4: Clean up
cd ..
rm -rf advent-mirror

echo "Migration completed successfully!"
```

### Option 3: Using the Interactive Script

If you prefer the interactive script with prompts:

```bash
# Clone this repository
git clone https://github.com/rostyantonov/advent-of-code
cd advent-of-code

# Run the migration script
./migrate-repository.sh

# When prompted, select option 2 (HTTPS with personal access token)
# Enter token: HuzzPDnVQ68xsmx1Gmj4
```

## Requirements

To run the migration, you need:
- ✅ Access to EPAM's corporate network (VPN or on-site)
- ✅ Git installed on your local machine
- ✅ The access token (provided above)
- ✅ Access to GitHub (to push the results)

## What Will Be Migrated

When you run the migration:
- ✅ All branches from EPAM GitLab → GitHub
- ✅ All tags
- ✅ Complete commit history
- ✅ All Advent of Code solutions in Kotlin

## Verification

After migration, verify all branches were transferred:

```bash
git clone https://github.com/rostyantonov/advent-of-code
cd advent-of-code
git fetch --all
git branch -r
```

You should see all branches from the EPAM repository.

## Security Note

⚠️ The access token in this document provides read access to the EPAM repository. After migration is complete, you may want to revoke this token from your EPAM GitLab settings.

## Next Steps After Migration

Once the migration is complete:
1. Verify all branches are present in GitHub
2. Consider revoking the EPAM GitLab access token
3. Update any CI/CD pipelines to point to the GitHub repository
4. Update documentation references from EPAM GitLab to GitHub
