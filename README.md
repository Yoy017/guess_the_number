# Java TCP programming - Practical content template

This repository contains the template to the
[Java TCP programming](https://github.com/heig-vd-dai-course/heig-vd-dai-course/blob/main/12-java-tcp-programming)
practical content.

# Get the application
First you need to pull the image from the GitHub Container Registry.
```sh
docker pull ghcr.io/yoy017/guess-the-number:latest
```

Then create a network.
```sh
# Create a Docker network
docker network create heig-vd-dai

# List of Docker networks
docker network list

# Delete a Docker network
docker network rm <name of the Docker network>
```

You will be able to run the server side of the application with this command
```sh
docker run --rm -it --network <name of your network> --name <name of the server> guess-the-number server
```

Then you can start in a new terminal a new client session with the command
```sh
docker run --rm -it --network <name of your network> --name <name of the server> guess-the-number client --host the-server
```

Enjoy !
