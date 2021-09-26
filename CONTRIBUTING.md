# Wanna contribute?
 
Here are the simple steps for contributing to this repo;

1. **READ CODE OF CONDUCT**

Read the [Code of Conduct](https://github.com/clubgamma/code-of-conduct) before starting contributing.

2. **Fork the repo**

Firstly fork the repo to your own GitHub account by clicking the Fork button on the top-right corner. Still can’t find it, check the image below.


![Screenshot 2021-09-26 at 12 59 29 AM](https://user-images.githubusercontent.com/58077762/93772626-ac77ef80-fc3c-11ea-8ee6-e381e1d68280.png)


After the successful fork, you'll acknowledge a copy of this repo on your own.
 
  3. **Clone the repo**
  
Now it's time to copy this repo to my own laptop/PC. To clone the repo you can write the below command in **Git Bash**

        > git clone <REPO_LINK_FROM_YOUR_ACCOUNT>
    
You can get the repo link from the Download section in the repo copied in your account. If you’re still facing problems check out the image below.
![Screenshot 2021-09-26 at 1 01 24 AM](https://user-images.githubusercontent.com/68161473/134783999-acc0fc65-60ed-4ad7-b9a9-5ff7c2b5c27f.png)




  4. **Set up remote repo**

- When you clone your fork, it will automatically set your fork as the "origin" remote. Use git remote -v to show your current remotes. You should see the URL of your fork (which you copied in step 3) next to the word "origin". 
If you don't see the "origin" remote, you can add it using the git command below.

        > git remote add origin <REPO_LINK_FROM_YOUR_ACCOUNT>

- Now you have to set up the upstream. write the following Git command to set up the upstream.
> git remote add upstream https://github.com/clubgamma/Random-Meme-Generator-App.git
 
- Now pull the latest changes from original repo to your local changes by firing the below command
        > git pull upstream master
  
5. **It's Code Time now**
  
After getting the project in code editor, make necessary changes keeping the features in mind available in readme.

   6. **Now it's time to save the work**

      - Make a branch for the feature you have worked on
        > git checkout -b feature_branch_name
      - Stage the changes you have made by firing the below command
        > git add .
      - Commit the changes 
        > git commit -m "Description of changes in your work"
      - Push the changes to your forked repo in that specific feature branch
        >  git push origin feature_branch_name
        
     Note: Create a branch for that feature and push there. No direct push to master. 
        
  7. **Let's finish this**
- Go to your forked repo on GitHub website and refresh the page, you'll see something like the below image
![Screenshot 2021-09-26 at 6 16 17 PM](https://user-images.githubusercontent.com/68161473/134808811-741fac41-fdf4-4883-891a-ae628be25e73.png)




- Click on pull-request and you will be redirected to another page where you will see something like below image 

![Screenshot 2021-09-26 at 6 16 36 PM](https://user-images.githubusercontent.com/68161473/134808817-fe9667e6-d34d-4c24-a9fd-c0a698aca37e.png)


  
- After that you have to write your GitHub username as the title of your pull-request and describe your work if you want and that's it!! Create a pull-request by clicking the button
Also add the below 2 lines in the description. It is compulsory for successful submission.
![Screenshot 2021-09-26 at 6 58 08 PM](https://user-images.githubusercontent.com/58077762/94461826-43fab680-01d8-11eb-96cd-80b2d69e13be.png)



 - [X] I have read the Code Of Conduct.
        
 - [X] I have followed all the steps of submission properly.


**Woohoo!! Congratulations on making your open source contribution🎉**
**Wait for some time to get your PR merged by our team**