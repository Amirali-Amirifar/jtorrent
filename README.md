# Overview
jTorrent is a simple Tor network simulation client and server written in Java. This client allows users to download and share files using the TCP protocol, which is a peer-to-peer file sharing protocol.
The Tracker (which acts as a mediator between the peers) keeps track of files in network and peers who are still connected.



## Features
* Multi-protocol Support: TCP for file sharing and UDP for tracking.
* Concurrent Connections: Allows many peers to be connected in the network at the same time.
* *(WIP)* File Verification: Verifies the integrity of downloaded files using SHA-1 hashes.
* Peer Management: Manages connections to multiple peers efficiently via Tracker.
* Cross-Platform: Runs on any platform with Java support.

## Requirements
* Java Development Kit (JDK) Latest Release
* Gradle (for dependency management and build)

## Installation

1. clone the repository 
   
```sh
git clone https://github.com/Amirali-Amirifar/jtorrent.git
cd jtorrent
```
2. Build the project
    ```./gradlew build```

3. run `TrackerMain` and `PeerMain` to connect start simulation.

#### Usage:
There are several commands to communicate to other peers and the tracker.

* `set_name <name>`
* `get_peers`
* `list_files`
* `share <filename>`
* `get <filename> <peerIp>:<peerTcpPort>`

### License
This project is licensed under the Gnu/GPL3 License. See the LICENSE file for details.

### Contact
For issues, questions, or contributions, please open an issue on GitHub or contact the repository owner directly.