def isThere(arr, o):
    flag = 0
    for ii in range(len(arr)):
        if(arr[ii] == o):
            flag = 1
            break
    return flag


def fifo():
    pageFrame = []
    pfl = 3  # page frame len
    hits = 0
    ptr = 0
    n = len(pages)
    for i in range(n):
        print('page = ', pages[i])
        if(isThere(pageFrame, pages[i]) == 0):  # page not in pageFrame
            if(len(pageFrame) < pfl):  # if the pageframe isn't full, just add the page in it
                pageFrame.append(pages[i])
            else:
                # we are replacing that page which has made the earliest entry
                pageFrame[ptr] = pages[i]
                ptr = (ptr+1) % pfl
        else:
            hits += 1
            print('page hit')
        print(pageFrame)
        print('')

    print('hits = ', hits)


def lru():
    hits = 0

    n = len(pages)

    pageFrame = []
    pfl = 3
    backD = []   # it maintain the backward dist of the pages
    for i in range(n):
        print('page = ', pages[i])
        # if the page is not there in the page frame
        if(isThere(pageFrame, pages[i]) == 0):
            if(len(pageFrame) < pfl):  # if the pageframe isn't full, just insert the pages
                pageFrame.append(pages[i])
                for j in range(len(backD)):
                    # the backward dist is incremented by 1 as we move ahead
                    backD[j] += 1
                # the backward dist of the newly inserted page is 0
                backD.append(0)
            else:
                # to find least recently used page which has the max backward dist
                lasoc = max(backD)
                ptr = backD.index(lasoc)
                pageFrame[ptr] = pages[i]
                for j in range(len(backD)):
                    backD[j] += 1
                backD[ptr] = 0
        else:
            t = 0
            hits += 1
            print('page hit')
            for j in range(len(pageFrame)):
                t = j
                break
            for j in range(len(backD)):
                backD[j] += 1
            # it maintains the backward distance to find the least recently used page
            backD[t] = 0
        print('pageframe = ', pageFrame)
        print('backward dist = ', backD)
        print('')
    print('hits = ', hits)


def findNextoc(arr, ch):   # to find the next occurence of a page in opr algorithm
    mx = 99999
    for j in range(len(arr)):
        if(arr[j] == ch):
            return j+1
    return mx


def opr():
    hits = 0
    pageFrame = []
    nextap = []
    pfl = 3
    for i in range(len(pages)):
        print('page = ', pages[i])
        # if the page is not present in the page frame
        if(isThere(pageFrame, pages[i]) == 0):
            if(len(pageFrame) < pfl):  # if the pagefram is not full, just add the page in it
                pageFrame.append(pages[i])
                # to find the next occurence of the page
                no = findNextoc(pages[i+1:], pages[i])
                for j in range(len(nextap)):
                    if(nextap[j] != 99999):
                        # the next occurence of the page is reduced by 1 as the next page comes closer
                        nextap[j] -= 1
                nextap.append(no)
            else:
                # it finds which page has its next page farthest from it
                lasoc = max(nextap)
                # it finds the index of that page from pageframe from nextap
                ptri = nextap.index(lasoc)
                # a new page has been inserted at that point where the prev page has its farthest dist from its next page
                pageFrame[ptri] = pages[i]
                # it finds the next occurence of the newly inserted page
                no = findNextoc(pages[i+1:], pages[i])
                for j in range(len(nextap)):
                    if(nextap[j] != 99999):
                        nextap[j] -= 1
                # the next appearence has been updated of the newly inserted page
                nextap[ptri] = no
        else:
            t = 0
            hits += 1
            print('page hit')
            for j in range(len(pageFrame)):
                if(pageFrame[j] == pages[i]):
                    t = j
                    break
            for j in range(len(nextap)):
                if(nextap[j] != 99999):
                    nextap[j] -= 1
            # to find the next appearence of the page
            no = findNextoc(pages[i+1:], pages[i])
            nextap[t] = no
        print('pgfrm = ', pageFrame)
        print('nextap = ', nextap)
        print('')
    print('hits = ', hits)


pages = [3, 4, 3, 2, 6, 3, 5, 6, 4, 3, 6, 3]
print('\nFIFO \n')
fifo()
print('\nLRU \n')
lru()
print('\nOPR\n')
opr()
