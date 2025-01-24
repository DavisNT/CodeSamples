Lesser-known Git cheatsheet
===============
A cheatsheet of lesser-known Git features and commands.

Version 0.9.1

## External links

- Official Git Reference: https://git-scm.com/docs
- How to Write a Git Commit Message: https://cbea.ms/git-commit/

## Git features & commands

### Shallow cloning

Git allows to clone a repository with reduced history depth.

- Clone latest commit of specified branch: [`git clone`](https://git-scm.com/docs/git-clone)` --depth 1 https://server/path/to/repo.git -b branch_name`
- [`git log`](https://git-scm.com/docs/git-log) will show only a single commit
- To download complete history do: [`git fetch`](https://git-scm.com/docs/git-fetch)` origin --unshallow`

### Submodules

Git allows to mount other repositories (e.g. dependencies) as folders in a Git repository.

See https://git-scm.com/docs/git-submodule and https://git-scm.com/docs/gitsubmodules for details.

### Finding problematic commit using bisect

Git can guide the user through a binary search for finding the commit which had introduced a problem. Binary search is extremely efficient - with this approach it is possible to find the problematic commit among 15 000 other commits by building and testing the software only 14 times (assuming that no builds or tests will fail due to unrelated issues).

See https://git-scm.com/docs/git-bisect for details.

### Upkeeping history

- When renaming/moving (preferably using [`git mv`](https://git-scm.com/docs/git-mv)) and modifying a file in the same commit, before committing use [`git status`](https://git-scm.com/docs/git-status) to check that Git will correctly record the rename (and not treat it as deletion and creation of another file).

### Viewing history

There are many interesting options for [`git log`](https://git-scm.com/docs/git-log) (many of which can be combined), including:

- View files altered in each commit: `git log --raw`
- View commit author and date: `git log --format=fuller`
- View text-based graphical representation of the history: `git log --graph`
- View only the history affecting a specific file/folder (e.g. `path`): `git log -- path`
  - A very useful combination: `git log --raw -- path`
- View history affecting a specific file (e.g. `filepath`), including renaming and history before renaming: `git log --raw --follow -- filepath`
- View only the history adding or removing lines matching a `regex`: `git log -G regex`
  - Very useful for finding commits adding/removing function calls.
- Show info about a commit: [`git show`](https://git-scm.com/docs/git-show)` --format=fuller commit`
- Compare files in two commits: [`git diff`](https://git-scm.com/docs/git-diff)` commit1 commit2`
  - Very useful to get a cumulative list of changes from a range of commits (in this case `commit1` should be the last commit before the range and `commit2` should be the last commit in the range).
  - Can be used to compare across branches.
- Annotate each line with date, author and commit hash: [`git blame`](https://git-scm.com/docs/git-blame)` -- path`

### Rewriting history

- Update the last commit (instead of creating a new commit): [`git commit`](https://git-scm.com/docs/git-commit)` --amend`
  - Useful when fixing issues with the last commit.
- Update the last commit and reset author information (incl. author date): `git commit --amend --reset-author`
- Rebase the current HEAD (currently checked-out version) on top of another branch (e.g. `upstream/master`): [`git rebase`](https://git-scm.com/docs/git-rebase)` upstream/master`
- Rebase another branch (e.g. `origin/feature1`) on top of the current HEAD (currently checked-out version): `git rebase HEAD origin/feature1`
  - Very useful to bundle several pull requests (or other feature branches) together for building and testing.
  - Actually does **not** rewrite history, but only appends commits from other branches on top of the current HEAD.
- Rebase the current HEAD (currently checked-out version) on top of another branch (e.g. `upstream/master`) using the interactive rebase: `git rebase -i upstream/master`
- Edit history (commits after `commit1`) using the interactive rebase: `git rebase -i commit1`
- Edit history (last `3` commits - as the first selected commit cannot be squashed the command uses `4` instead of `3`) using the interactive rebase: `git rebase -i HEAD~4`
- All history rewrites need to be force-pushed with: [`git push`](https://git-scm.com/docs/git-push)` -f`
  - or, as a safer alternative for **some cases**: `git push --force-with-lease`
  - Before any force-pushing it is a good idea to check which remote branch is being tracked: `git status`

### Restoring deleted file(s)

To restore previously existing file/folder (e.g. `path`):

- View the history of `path` using: `git log --raw --follow -- path`
- Decide which version to restore (usually it will be second commit from the top, because the first commit from the top will be deletion)
- Using the `commit` from previous step invoke: [`git restore`](https://git-scm.com/docs/git-restore)` -S -W -s commit -- path`

The restored file(s) in `path` should have all associated history.

### Recovering after incorrectly rewriting history

- Backup the repository folder (just in case - especially if it can be easily done)!
- View reference log (previous "versions" of HEAD - previously checked-out versions/commits) using: [`git reflog`](https://git-scm.com/docs/git-reflog)
- Determine the correct version
  - To view history of each `commit` use: `git log --raw --format=fuller commit`
- When the correct `commit` for recovering to has been identified, check it out using: [`git switch`](https://git-scm.com/docs/git-switch)` --detach commit`
- Verify that history (using `git log --raw --format=fuller`) and the files are correct
- Create the necessary `branch` using: `git switch -c branch`

### Misc

- To list remotes (`origin`, `upstream` etc.) of the repository (and their URLs): [`git remote`](https://git-scm.com/docs/git-remote)` -v`
- To apply a specific `commit` on top of the current HEAD (currently checked-out version): [`git cherry-pick`](https://git-scm.com/docs/git-cherry-pick)` commit`
- To create a new `branch` from specific remote branch (e.g. `upstream/master`):
  - `git switch --detach upstream/master`
  - `git switch -c branch`
